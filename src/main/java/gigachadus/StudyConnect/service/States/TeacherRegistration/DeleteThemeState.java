package gigachadus.StudyConnect.service.States.TeacherRegistration;

import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

public class DeleteThemeState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Введите тему диплома для удаления:");
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            List<Map<String, Object>> topics = telegramBot.getTopics(chatId);
            topics.removeIf(t -> t.get(StateConfiguration.THEME_NAME_STATE).equals(text));
            telegramBot.setCurrentState(chatId, StateConfiguration.DIPLOMA_TOPICS_STATE);
        }
    }
}
