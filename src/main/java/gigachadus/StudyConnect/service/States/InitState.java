package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InitState implements State {

    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "/start?");
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equalsIgnoreCase("/start")){
                telegramBot.setCurrentState(chatId, StateConfiguration.NAME_REGISTRATION_STATE);
            }else {
                telegramBot.setCurrentState(chatId, StateConfiguration.INIT_STATE);
            }
        }
    }
}
