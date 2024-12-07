package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartState implements State{

    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Бу! Испугался? Не бойся меня. Ты боишься меня?");
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equalsIgnoreCase("да")) {
                telegramBot.sendMessage(chatId, "Не бойся меня. Пиши, когда будешь готов");
                telegramBot.setCurrentState(chatId, StateConfiguration.INIT_STATE);
            } else {
                telegramBot.sendMessage(chatId, "Тогда начнём!");
                telegramBot.setCurrentState(chatId, StateConfiguration.STUD_INIT_THEME_STATE);
            }
        }
    }
}
