package gigachadus.StudyConnect.service;

import gigachadus.StudyConnect.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    final AccountManager accountManager;

    public TelegramBot(BotConfig config, AccountManager accountManager) {
        super(config.getBotToken());
        this.config = config;
        this.accountManager = accountManager;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            switch (messageText) {
                case "/start" -> {
                    sendMessage(update.getMessage().getChatId(), accountManager.greeting());
                }
                default -> {
                    sendMessage(update.getMessage().getChatId(), "Unsupported message(((");
                }
            }
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

    @Override
    public void onRegister() {
        super.onRegister();
    }

    private void sendMessage(Long chatId, String text) {
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
