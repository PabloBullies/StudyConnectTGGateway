package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.State;
import gigachadus.StudyConnect.service.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InitState implements State {

    @Override
    public void questionState(Update update, TelegramBot telegramBot) {
        telegramBot.sendMessage(update.getMessage().getChatId(), "/start?");
    }

    @Override
    public void answerState(Update update, TelegramBot telegramBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equalsIgnoreCase("/start")){
                telegramBot.setCurrentState(update.getMessage().getChatId(), StateConfiguration.START_STATE,update);
            }
        }else {
            telegramBot.setCurrentState(update.getMessage().getChatId(), StateConfiguration.START_STATE, update);
        }
    }
}
