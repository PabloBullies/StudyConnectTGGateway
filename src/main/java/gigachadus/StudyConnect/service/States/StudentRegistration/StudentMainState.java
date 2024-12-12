package gigachadus.StudyConnect.service.States.StudentRegistration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import gigachadus.StudyConnect.service.repository.DiplomaTopicResponse;
import gigachadus.StudyConnect.service.repository.MentorResponseWithIsApprove;
import gigachadus.StudyConnect.service.repository.StudentResponseWithIsApprove;
import gigachadus.StudyConnect.service.repository.SuggestMentorResponse;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.*;

public class StudentMainState implements State {

    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("Искать руководителя").callbackData("/show_mentors").build());
        rowsInline.add(rowInline);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(InlineKeyboardButton.builder().text("Просмотр взаимностей").callbackData("/show_accepted_mentors").build());
        rowsInline.add(rowInline2);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(InlineKeyboardButton.builder().text("Зарегистрироваться заново").callbackData("/registration").build());
        rowsInline.add(rowInline3);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        telegramBot.sendMessage(chatId, "Что хотите сделать?", inlineKeyboardMarkup);
    }


    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            switch (callbackData){
                case "/show_mentors":
                    data.put("DB_PAGE", 0);
                    telegramBot.setCurrentState(chatId, StateConfiguration.SHOW_ALL_MENTORS_STATE);
                    break;
                case "/show_accepted_mentors":
                    try{
                        HttpResponse<String> response = telegramBot.getDataSender().getMatchingMentors((String) data.get("BD_ID"));
                        if (response.statusCode() != 200){
                            System.err.println(response);
                        }
                        Gson gson = new Gson();
                        Type acceptedMentorsType = new TypeToken<List<MentorResponseWithIsApprove>>(){}.getType();
                        List<MentorResponseWithIsApprove> acceptedMentors = gson.fromJson(response.body(), acceptedMentorsType);
                        data.put(StateConfiguration.SHOW_ACCEPTED_MENTORS_STATE, acceptedMentors);
                        telegramBot.setCurrentState(chatId, StateConfiguration.SHOW_ACCEPTED_MENTORS_STATE);
                    }catch (Exception e){
                        System.err.println(e);
                    }
                    break;
                case "/registration":
                    telegramBot.clearUsersData(chatId);
                    telegramBot.setCurrentState(chatId, StateConfiguration.NAME_REGISTRATION_STATE);
                    break;
            }
        }
    }
}
