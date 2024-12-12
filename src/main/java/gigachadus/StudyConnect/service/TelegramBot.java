package gigachadus.StudyConnect.service;

import gigachadus.StudyConnect.config.BotConfig;
import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.repository.DataSender;
import jakarta.inject.Singleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
@Singleton
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;


    private final Map<Long, String> userStates = new HashMap<>();
    private final Map<Long, Map<String, Object>> usersData = new HashMap<>();
    private ApplicationContext applicationContext;
    private final DataSender dataSender;

    public Map<String, Object> getUserEnteredData(Long chatId) {
        if (usersData.containsKey(chatId)){
            return usersData.get(chatId);
        }else{
            Map<String, Object> data = new HashMap<>();
            usersData.put(chatId, data);
            return data;
        }
    }

    public List<Map<String, Object>> getTopics(Long chatId){
        Map<String, Object> data = this.getUserEnteredData(chatId);
        ArrayList<Map<String, Object>> topics = (ArrayList<Map<String, Object>>) data.getOrDefault("DIPLOMA_TOPICS", new ArrayList<Map<String, Object>>());
        data.put("DIPLOMA_TOPICS", topics);
        return topics;
    }

    public List<Map<String, Object>> getTopics(Map<String, Object> data){
        ArrayList<Map<String, Object>> topics = (ArrayList<Map<String, Object>>) data.getOrDefault("DIPLOMA_TOPICS", new ArrayList<Map<String, Object>>());
        data.put("DIPLOMA_TOPICS", topics);
        return topics;
    }

    public Map<String, Object> getCurrentTopic(Long chatId){
        Map<String, Object> data = this.getUserEnteredData(chatId);
        List<Map<String, Object>> topics = this.getTopics(chatId);

        String current_theme = (String) data.get("CURRENT_THEME_NAME");
        Optional<Map<String, Object>> topic = topics.stream().filter(x -> {
            String topic_name = (String) x.get(StateConfiguration.THEME_NAME_STATE);
            if (topic_name.equals(current_theme)){
                return true;
            }
            return false;

        }).findFirst();
        if (topic.isEmpty()){
            Map<String, Object> newTopic = new HashMap<String, Object>();
            setTopic(newTopic, chatId);
            return newTopic;
        }
        Map<String, Object> topicValue = topic.get();
        return topicValue;
    }

    public void setCurrentTopicName(Long chatId, String name){
        Map<String, Object> data = this.getUserEnteredData(chatId);
        data.put("CURRENT_THEME_NAME", name);
    }

    public void setTopic(Map<String, Object> newTopic, Long chatId){
        List<Map<String, Object>> topics = this.getTopics(chatId);
        topics.removeIf(t -> {
            String topic_name = (String) t.get(StateConfiguration.THEME_NAME_STATE);
            String new_topic_name = (String) newTopic.get(StateConfiguration.THEME_NAME_STATE);
            if (topic_name.equals(new_topic_name)){
                return true;
            }
            return false;
        });
        topics.add(newTopic);
    }

    public void clearUsersData(Long chatId) {
        Map<String, Object> data = usersData.remove(chatId);
    }

    public TelegramBot(BotConfig config, ApplicationContext applicationContext) {
        super(config.getBotToken());
        this.config = config;
        this.applicationContext = applicationContext;
        this.dataSender = new DataSender(this.config);
    }

    public DataSender getDataSender() {
        return this.dataSender;
    }

    @Override
    public void onUpdateReceived(Update update) {
        int a = 1;
        Long chatId = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
             chatId = update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        String currentState = userStates.getOrDefault(chatId, StateConfiguration.INIT_STATE);
        State state = (State) applicationContext.getBean(currentState);
        if (chatId != null){
            state.answerState(chatId, update, this);
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

    public void setCurrentState(long chatId, String newState) {
        userStates.put(chatId, newState);
        State state = (State) applicationContext.getBean(newState);
        state.questionState(chatId, this);
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
    public void sendMessage(Long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
