package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

public class EmailRegistrationState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Введи свой email");
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Map<String, Object> data = telegramBot.getUserEnteredData(update.getMessage().getChatId());
            data.put(StateConfiguration.EMAIL_REGISTRATION_STATE, text);
            telegramBot.setCurrentState(chatId, StateConfiguration.DEPARTMENT_REGISTRATION_STATE);
        }
    }
}
