package gigachadus.StudyConnect.service.States.TeacherRegistration;

import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class DiplomaTopicsState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("Посмотреть список").callbackData("/check_themes").build());
        rowInline.add(InlineKeyboardButton.builder().text("Добавить тему").callbackData("/add_theme").build());

        rowsInline.add(rowInline);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(InlineKeyboardButton.builder().text("Удалить тему").callbackData("/del_theme").build());
        rowInline2.add(InlineKeyboardButton.builder().text("Закончить регистрацию").callbackData("/end").build());

        rowsInline.add((rowInline2));

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        telegramBot.sendMessage(chatId, "Что хотите сделать со своими темами для диплома?", inlineKeyboardMarkup);
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();

            switch (callbackData) {
                case "/check_themes":
                    for (Map<String, Object> topic: telegramBot.getTopics(chatId)){
                        telegramBot.sendMessage(chatId, topic.toString());
                    }
                    telegramBot.setCurrentState(chatId, StateConfiguration.DIPLOMA_TOPICS_STATE);
                    break;
                case "/add_theme":
                    telegramBot.setCurrentState(chatId, StateConfiguration.THEME_NAME_STATE);
                    break;
                case "/del_theme":
                    telegramBot.setCurrentState(chatId, StateConfiguration.DELETE_TOPIC_STATE);
                    break;
                case "/again":
                    telegramBot.setCurrentState(chatId, StateConfiguration.DIPLOMA_TOPICS_STATE);
                    break;
                case "/correct":
                    telegramBot.setCurrentState(chatId, StateConfiguration.FINISH_REGISTRATION_STATE);
                    break;
                case "/end":
                    System.out.println(telegramBot.getUserEnteredData(chatId).toString());
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(InlineKeyboardButton.builder().text("Да").callbackData("/correct").build());
                    rowInline.add(InlineKeyboardButton.builder().text("Начать заново").callbackData("/again").build());
                    rowInline.add(InlineKeyboardButton.builder().text("Зарегистрироваться заново").callbackData("/registration").build());
                    rowsInline.add(rowInline);
                    inlineKeyboardMarkup.setKeyboard(rowsInline);
                    telegramBot.sendMessage(chatId, "Закончить регистрацию?", inlineKeyboardMarkup);
                    break;
            }

        }
    }
}
