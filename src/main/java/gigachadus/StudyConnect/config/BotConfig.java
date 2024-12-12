package gigachadus.StudyConnect.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    private final String botName;

    private final String botToken;

    private final String masterIp;

    public BotConfig(@Value("${bot.name}") String botName, @Value("${bot.token}") String botToken, @Value("${master.uri}") String masterIp) {
        this.botName = botName;
        this.botToken = botToken;
        this.masterIp = masterIp;
    }



    public String getBotName() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getMasterIp() {
        return masterIp;
    }
}
