package gigachadus.StudyConnect.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface State {
    void questionState(Update update, TelegramBot telegramBot);
    void answerState(Update update, TelegramBot telegramBot);

}
