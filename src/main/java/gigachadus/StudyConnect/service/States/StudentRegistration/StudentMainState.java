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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentMainState implements State {

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
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(InlineKeyboardButton.builder().text("Начать сначала").callbackData("/again").build());

                rowsInline.add(rowInline);
                inlineKeyboardMarkup.setKeyboard(rowsInline);
                data.put("DB_PAGE", 0);
                telegramBot.sendMessage(chatId, "Нет подходящих рекомендаций. Предлагаем начать поиск с начала", inlineKeyboardMarkup);
            }else{
                Gson gson = new Gson();
                SuggestMentorResponse suggestMentorResponse = gson.fromJson(response.body(), SuggestMentorResponse.class);
                suggestMentor(chatId, suggestMentorResponse, telegramBot);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        telegramBot.sendMessage(chatId, mentor, inlineKeyboardMarkup);
    }


    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);

            switch (callbackData) {
                case "/again":
                    data.put("BD_PAGE", 0);
                    telegramBot.setCurrentState(chatId, StateConfiguration.STUDENT_MAIN_STATE);
                    break;
                case "/next":
                    Integer page = (Integer) data.getOrDefault("BD_PAGE", 0);
                    System.out.println("PAGE" + page);
                    data.put("BD_PAGE", page + 1);
                    telegramBot.setCurrentState(chatId, StateConfiguration.STUDENT_MAIN_STATE);
                    break;
                case "/like":
                    // TODO add like route
                    page = (Integer) data.getOrDefault("BD_PAGE", 0);
                    System.out.println("PAGE" + page);
                    data.put("BD_PAGE", page + 1);
                    telegramBot.setCurrentState(chatId, StateConfiguration.STUDENT_MAIN_STATE);
                    break;
            }

        }
    }
}
