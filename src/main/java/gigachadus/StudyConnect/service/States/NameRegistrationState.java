package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

public class NameRegistrationState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Введи своё имя");
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message msg = update.getMessage();
            String text = msg.getText();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            data.put(StateConfiguration.NAME_REGISTRATION_STATE, text);
            String tg_id = msg.getFrom().getUserName();
            if (tg_id == null){
                telegramBot.clearUsersData(chatId);
                telegramBot.sendMessage(chatId, "Для регистрации необходимо иметь telegram nickname (который начинается с \"@)\"");
                telegramBot.setCurrentState(chatId, StateConfiguration.NAME_REGISTRATION_STATE);
            }
            data.put(StateConfiguration.TG_NICKNAME, "@" + tg_id);
            telegramBot.setCurrentState(chatId, StateConfiguration.EMAIL_REGISTRATION_STATE);
        }
    }
}
