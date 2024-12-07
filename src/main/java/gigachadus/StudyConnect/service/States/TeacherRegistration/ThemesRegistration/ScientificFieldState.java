package gigachadus.StudyConnect.service.States.TeacherRegistration.ThemesRegistration;

import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

public class ScientificFieldState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Введи научную область");
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Map<String, Object> topic = telegramBot.getCurrentTopic(chatId);

            topic.put(StateConfiguration.THEME_SCIENTIFIC_FIELDS_STATE, text);
            telegramBot.setTopic(topic, chatId);
            telegramBot.setCurrentState(chatId, StateConfiguration.THEME_SUMMARY_STATE);
        }
    }
}
