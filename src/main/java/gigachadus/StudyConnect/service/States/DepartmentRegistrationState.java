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

        for (Map.Entry<String, String> entry : StateConfiguration.allFaculties.entrySet()) {
            String faculty = entry.getKey();
            String tag = entry.getValue();

            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            rowInline.add(InlineKeyboardButton.builder().text(faculty).callbackData(tag).build());

            rowsInline.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(InlineKeyboardButton.builder().text("Закончил вводить").callbackData("/end").build());

        rowsInline.add((rowInline2));

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        telegramBot.sendMessage(chatId, "Выбери свои факультет:", inlineKeyboardMarkup);
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            String department = (String) data.getOrDefault(StateConfiguration.DEPARTMENT_REGISTRATION_STATE, "");
            switch (callbackData) {
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
                    telegramBot.sendMessage(chatId, "Всё верно?: " + StateConfiguration.getKey(StateConfiguration.allFaculties, department), inlineKeyboardMarkup);
                    break;
                default:
                    if (StateConfiguration.allFaculties.containsValue(callbackData)){
                        data.put(StateConfiguration.DEPARTMENT_REGISTRATION_STATE, callbackData);
                    }
            }
        }
    }
}
