package gigachadus.StudyConnect.service;

import gigachadus.StudyConnect.config.BotConfig;
import gigachadus.StudyConnect.service.States.RegistrationState;
import jakarta.activation.DataHandler;
import jakarta.inject.Singleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Singleton
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;



    private final Map<Long, String> userStates = new HashMap<>();
    private ApplicationContext applicationContext;


    public TelegramBot(BotConfig config, ApplicationContext applicationContext) {
        super(config.getBotToken());
        this.config = config;

        this.applicationContext = applicationContext;

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String currentState = userStates.getOrDefault(chatId, StateConfiguration.INIT_STATE);
            State state = (State) applicationContext.getBean(currentState);
            state.answerState(update, this);
            state.questionState(update, this);

        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    public void setCurrentState(long chatId, String newState, Update update) {

        userStates.put(chatId, newState);
        State state = (State) applicationContext.getBean(newState);
        state.answerState(update, this);
    }
    @Override
    public void onRegister() {
        super.onRegister();
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
