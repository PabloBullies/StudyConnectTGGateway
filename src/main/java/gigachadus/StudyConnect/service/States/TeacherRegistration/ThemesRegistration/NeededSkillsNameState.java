package gigachadus.StudyConnect.service.States.TeacherRegistration.ThemesRegistration;

import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class NeededSkillsNameState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Map.Entry<String, String> entry : StateConfiguration.allSkillTag.entrySet()) {
            String skill = entry.getKey();
            String tag = entry.getValue();

            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            rowInline.add(InlineKeyboardButton.builder().text(skill).callbackData(tag).build());

            rowsInline.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(InlineKeyboardButton.builder().text("Закончил вводить").callbackData("/end").build());

        rowsInline.add((rowInline2));

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        telegramBot.sendMessage(chatId, "Выбери необходимые для диплома skills:", inlineKeyboardMarkup);
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Map<String, Object> topic = telegramBot.getCurrentTopic(chatId);

            Set<String> needed_skills = (Set<String>) topic.getOrDefault(StateConfiguration.THEME_NEEDED_SKILLS_STATE, new HashSet<String>());
            switch (callbackData){
                case "/correct":
                    telegramBot.setCurrentState(chatId, StateConfiguration.THEME_SCIENTIFIC_FIELDS_STATE);
                    break;
                case "/again":
                    needed_skills.clear();
                    this.questionState(chatId, telegramBot);
                    break;
                case "/registration":
                    telegramBot.clearUsersData(chatId);
                    telegramBot.setCurrentState(chatId, StateConfiguration.NAME_REGISTRATION_STATE);
                    break;
                case "/end":
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(InlineKeyboardButton.builder().text("Да").callbackData("/correct").build());
                    rowInline.add(InlineKeyboardButton.builder().text("Начать заново").callbackData("/again").build());
                    rowInline.add(InlineKeyboardButton.builder().text("Зарегистрироваться заново").callbackData("/registration").build());
                    rowsInline.add(rowInline);
                    inlineKeyboardMarkup.setKeyboard(rowsInline);
                    telegramBot.sendMessage(chatId, "Всё верно?: " + needed_skills.toString().replace("{", "").replace("}", ""), inlineKeyboardMarkup);
                    break;
                default:
                    if (StateConfiguration.allSkillTag.containsValue(callbackData)){
                        needed_skills.add(callbackData);
                    }
            }
            topic.put(StateConfiguration.THEME_NEEDED_SKILLS_STATE, needed_skills);
            telegramBot.setTopic(topic, chatId);
        }
    }
}
