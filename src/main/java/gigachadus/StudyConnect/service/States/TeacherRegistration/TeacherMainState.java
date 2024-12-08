package gigachadus.StudyConnect.service.States.TeacherRegistration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import gigachadus.StudyConnect.service.repository.DiplomaTopicResponse;
import gigachadus.StudyConnect.service.repository.StudentResponseWithIsApprove;
import gigachadus.StudyConnect.service.repository.SuggestMentorResponse;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TeacherMainState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("Проверить лайки").callbackData("/show_approving_students").build());


        rowsInline.add(rowInline);


        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("Редактировать свои темы").callbackData("/edit_themes").build());

        rowsInline.add((rowInline2));

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("Зарегистрироваться заново").callbackData("/registration").build());

        rowsInline.add((rowInline3));

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        telegramBot.sendMessage(chatId, "Что хотите сделать?", inlineKeyboardMarkup);
    }


    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            HashSet<String> interests = (HashSet<String>) data.getOrDefault(StateConfiguration.INTERESTS_STATE, new HashSet());
            switch (callbackData){
                case "/show_approving_students":
                    try{
                        HttpResponse<String> response = telegramBot.getDataSender().getMatchingStudents((String) data.get("BD_ID"));
                        if (response.statusCode() != 200){
                            System.err.println(response);
                        }
                        Gson gson = new Gson();
                        Type approvingStudentsType = new TypeToken<List<StudentResponseWithIsApprove>>(){}.getType();
                        List<StudentResponseWithIsApprove> approvingStudents = gson.fromJson(response.body(), approvingStudentsType);
                        data.put(StateConfiguration.SHOW_APPROVING_STUDENTS_STATE, approvingStudents);
                        telegramBot.setCurrentState(chatId, StateConfiguration.SHOW_APPROVING_STUDENTS_STATE);
                    }catch (Exception e){
                        System.err.println(e);
                    }
                    break;
                case "/edit_themes":
                    telegramBot.setCurrentState(chatId, StateConfiguration.DIPLOMA_TOPICS_STATE);
                    break;
                case "/registration":
                    telegramBot.clearUsersData(chatId);
                    telegramBot.setCurrentState(chatId, StateConfiguration.NAME_REGISTRATION_STATE);
                    break;
            }
        }
    }
}
