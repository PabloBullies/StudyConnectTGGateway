package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DepartmentRegistrationState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("ФИТ").callbackData("/FIT").build());
        rowInline.add(InlineKeyboardButton.builder().text("ФИЯ").callbackData("/FIJA").build());

        rowsInline.add(rowInline);

        List<InlineKeyboardButton> rowInline2= new ArrayList<>();
        rowInline2.add(InlineKeyboardButton.builder().text("ФФ").callbackData("/FF").build());
        rowInline2.add(InlineKeyboardButton.builder().text("ММФ").callbackData("/MMF").build());

        rowsInline.add(rowInline2);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        rowInline3.add(InlineKeyboardButton.builder().text("ФЖ").callbackData("/FG").build());
        rowInline3.add(InlineKeyboardButton.builder().text("ИИР").callbackData("/IIR").build());

        rowsInline.add(rowInline3);


        List<InlineKeyboardButton> rowInlineN = new ArrayList<>();
        rowInlineN.add(InlineKeyboardButton.builder().text("Закончил вводить").callbackData("/end").build());
        rowsInline.add(rowInlineN);
        inlineKeyboardMarkup.setKeyboard(rowsInline);

        telegramBot.sendMessage(chatId, "Выбери свой факультет:", inlineKeyboardMarkup);
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            String department = (String) data.getOrDefault(StateConfiguration.DEPARTMENT_REGISTRATION_STATE, "");
            switch (callbackData) {
                case "/FIT":
                    department = callbackData;
                    break;
                case "/FIJA":
                    department = callbackData;
                    break;
                case "/FF":
                    department = callbackData;
                    break;
                case "/MMF":
                    department = callbackData;
                    break;
                case "/FG":
                    department = callbackData;
                    break;
                case "/IIR":
                    department = callbackData;
                    break;
                case "/correct":
                    telegramBot.setCurrentState(chatId, StateConfiguration.INTERESTS_STATE);
                    break;
                case "/again":
                    this.questionState(chatId, telegramBot);
                    break;
                case "/registration":
                    telegramBot.clearUsersData(chatId);
                    telegramBot.setCurrentState(chatId, StateConfiguration.NAME_REGISTRATION_STATE);
                    break;
                case "/end":
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(InlineKeyboardButton.builder().text("Да").callbackData("/correct").build());
                    rowInline.add(InlineKeyboardButton.builder().text("Начать заново").callbackData("/again").build());
                    rowInline.add(InlineKeyboardButton.builder().text("Зарегистрироваться заново").callbackData("/registration").build());
                    rowsInline.add(rowInline);
                    inlineKeyboardMarkup.setKeyboard(rowsInline);
                    telegramBot.sendMessage(chatId, "Всё верно?: " + department, inlineKeyboardMarkup);
                    break;
            }
            data.put(StateConfiguration.DEPARTMENT_REGISTRATION_STATE, department);
        }
    }
}
