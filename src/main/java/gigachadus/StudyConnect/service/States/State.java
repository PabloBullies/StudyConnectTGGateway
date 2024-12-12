package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface State {
    void questionState(Long chatId, TelegramBot telegramBot);
    void answerState(Long chatId, Update update, TelegramBot telegramBot);

}
