package gigachadus.StudyConnect.config;

import gigachadus.StudyConnect.service.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
public class BotConfig {
    String botName;

    String botToken;

    public BotConfig(@Value("${bot.name}") String botName, @Value("${bot.token}") String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }

    public String getBotName() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }
}
