package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.State;
import gigachadus.StudyConnect.service.StateConfiguration;
import gigachadus.StudyConnect.service.StateName;
import gigachadus.StudyConnect.service.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.objects.Update;


public class RegistrationState implements State {

    @Override
    public void questionState(Update update, TelegramBot telegramBot) {
        Long chatId = update.getMessage().getChatId();
        telegramBot.sendMessage(chatId, "Давай познакомимся поближе...");
    }

    @Override
    public void answerState(Update update, TelegramBot telegramBot) {

    }
}
