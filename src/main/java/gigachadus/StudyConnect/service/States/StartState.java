package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.State;
import gigachadus.StudyConnect.service.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartState implements State{

    @Override
    public void questionState(Update update, TelegramBot telegramBot) {
        telegramBot.sendMessage(update.getMessage().getChatId(), "Бу! Испугался? Не бойся меня. Ты боишься меня?");
        telegramBot.setCurrentState(update.getMessage().getChatId(), StateConfiguration.REGISTRATION_STATE, update);
    }

    @Override
    public void answerState(Update update, TelegramBot telegramBot) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equalsIgnoreCase("да")) {
                telegramBot.sendMessage(update.getMessage().getChatId(), "Не бойся меня. Пиши, когда будешь готов");
                telegramBot.setCurrentState(update.getMessage().getChatId(), StateConfiguration.INIT_STATE, update);
            } else {

            }
        }
    }
}
