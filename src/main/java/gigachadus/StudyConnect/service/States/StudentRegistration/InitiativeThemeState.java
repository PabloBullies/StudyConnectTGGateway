package gigachadus.StudyConnect.service.States.StudentRegistration;

import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

public class InitiativeThemeState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Можешь ввесть свои пожелания. Может у тебя есть тема?");
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            data.put(StateConfiguration.STUD_INIT_THEME_STATE, text);
            telegramBot.setCurrentState(chatId, StateConfiguration.FINISH_REGISTRATION_STATE);
        }
    }
}
