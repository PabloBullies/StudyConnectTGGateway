package gigachadus.StudyConnect.service.States.StudentRegistration;

import com.google.gson.Gson;
import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import gigachadus.StudyConnect.service.repository.DiplomaTopicResponse;
import gigachadus.StudyConnect.service.repository.SuggestMentorResponse;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowAllMentors implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Поиск подходящих рекомендаций...");
        Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
        try{
            HttpResponse<String> response = telegramBot.getDataSender().getMentorSuggestion((String) data.get("BD_ID"), (Integer) data.get("BD_PAGE"));
            if (response.statusCode() != 200){
                System.err.println(response.body());
            }
            if (response.body().isEmpty()){
                data.put("DB_PAGE", 0);
                telegramBot.sendMessage(chatId, "Подходящие рекомендации закочились. Рекомендуем повторить поиск позже");
                telegramBot.setCurrentState(chatId, StateConfiguration.STUDENT_MAIN_STATE);
            }else{
                Gson gson = new Gson();

                SuggestMentorResponse suggestMentorResponse = gson.fromJson(response.body(), SuggestMentorResponse.class);
                suggestMentor(chatId, suggestMentorResponse, telegramBot);
                data.put("CURRENT_SUGGESTION", suggestMentorResponse);
            }

        } catch (InterruptedException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }


    }

    private void suggestMentor(Long chatId, SuggestMentorResponse suggestMentorResponse, TelegramBot telegramBot){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Имя: ").append(suggestMentorResponse.name()).append("\n");
        stringBuilder.append(suggestMentorResponse.department()).append("\n");
        stringBuilder.append("Интересы: ").append("\n");
        for(String interest: suggestMentorResponse.scientificInterests()){
            stringBuilder.append(StateConfiguration.getKey(StateConfiguration.allScientificFields, interest)).append(" ");
        }
        stringBuilder.append("\n");
        stringBuilder.append("Темы: ").append("\n");
        for(DiplomaTopicResponse topic : suggestMentorResponse.diplomaTopics()){
            stringBuilder.append("Тема: ").append(topic.name()).append("\n");
            stringBuilder.append("Необходимые навыки: ");
            for(String skill : topic.neededSkills()){
                stringBuilder.append(StateConfiguration.getKey(StateConfiguration.allSkillTag, skill)).append(" ");
            }
            stringBuilder.append("\n");
        }
        String mentor = stringBuilder.toString();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("Лайк").callbackData("/like").build());
        rowInline.add(InlineKeyboardButton.builder().text("Следующий").callbackData("/next").build());
        rowsInline.add(rowInline);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("На главную").callbackData("/to_main").build());
        rowsInline.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInline);
        telegramBot.sendMessage(chatId, mentor, inlineKeyboardMarkup);
    }


    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            Integer page = (Integer) data.getOrDefault("BD_PAGE", 0);
            String studentId = (String) data.get("BD_ID");

            SuggestMentorResponse suggestMentorResponse = (SuggestMentorResponse) data.get("CURRENT_SUGGESTION");
            switch (callbackData) {
                case "/to_main":
                    telegramBot.setCurrentState(chatId, StateConfiguration.STUDENT_MAIN_STATE);
                case "/next":
                    data.put("BD_PAGE", page + 1);
                    telegramBot.setCurrentState(chatId, StateConfiguration.SHOW_ALL_MENTORS_STATE);
                    break;
                case "/like":
                    try{
                        telegramBot.getDataSender().matchMentor(studentId, suggestMentorResponse.id());
                        data.put("BD_PAGE", page + 1);
                        telegramBot.setCurrentState(chatId, StateConfiguration.SHOW_ALL_MENTORS_STATE);
                    }catch (Exception e){
                        System.err.println(e);
                    }
                    break;
            }

        }
    }
}
