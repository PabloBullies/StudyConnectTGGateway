package gigachadus.StudyConnect.service.States.TeacherRegistration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.TelegramBot;
import gigachadus.StudyConnect.service.repository.StudentResponseWithIsApprove;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ShowApprovingStudents implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
        List<StudentResponseWithIsApprove> approvingStudents = (List<StudentResponseWithIsApprove>) data.get(StateConfiguration.SHOW_APPROVING_STUDENTS_STATE);
        if (approvingStudents.isEmpty()){
            telegramBot.sendMessage(chatId, "Рекомендаций закончились. Рекомендуем повторить поиск позже.");
            telegramBot.setCurrentState(chatId, StateConfiguration.TEACHER_MAIN_STATE);

        }else{
            StudentResponseWithIsApprove student = approvingStudents.get(0);
            suggestStudent(chatId, telegramBot, student);
        }

    }

    private void suggestStudent(Long chatId, TelegramBot telegramBot, StudentResponseWithIsApprove student){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Имя: ").append(student.name()).append("\n");
        stringBuilder.append(student.department()).append("\n");

        stringBuilder.append("Интересы: ").append("\n");
        for(String interest: student.scientificInterests()){
            stringBuilder.append(StateConfiguration.getKey(StateConfiguration.allScientificFields, interest)).append(" ");
        }
        stringBuilder.append("\n");
        stringBuilder.append("Навыки: ").append("\n");
        for(String skill: student.skills()){
            stringBuilder.append(StateConfiguration.getKey(StateConfiguration.allSkillTag, skill)).append(" ");
        }
        stringBuilder.append("\n");
        stringBuilder.append("Пожелания: ").append(student.initiativeTheme());

        String mentor = stringBuilder.toString();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("Лайк").callbackData("/like").build());
        rowInline.add(InlineKeyboardButton.builder().text("Следующий").callbackData("/next").build());
        rowsInline.add(rowInline);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline.add(InlineKeyboardButton.builder().text("Закончить просмотр").callbackData("/stop_watching").build());
        rowsInline.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInline);
        telegramBot.sendMessage(chatId, mentor, inlineKeyboardMarkup);
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            StudentResponseWithIsApprove student = ((List<StudentResponseWithIsApprove>) data.get(StateConfiguration.SHOW_APPROVING_STUDENTS_STATE)).remove(0);
            switch (callbackData){
                case "/like":
                    try{
                        telegramBot.getDataSender().matchStudent((String) data.get("BD_ID"), student.id(), true);
                    }catch (Exception e){
                        System.err.println(e);
                    }
                    telegramBot.setCurrentState(chatId, StateConfiguration.SHOW_APPROVING_STUDENTS_STATE);
                    break;
                case "/next":
                    try{
                        telegramBot.getDataSender().matchStudent((String) data.get("BD_ID"), student.id(), false);
                    }catch (Exception e){
                        System.err.println(e);
                    }
                    telegramBot.setCurrentState(chatId, StateConfiguration.SHOW_APPROVING_STUDENTS_STATE);
                    break;
                case "/stop_watching":
                    telegramBot.setCurrentState(chatId, StateConfiguration.TEACHER_MAIN_STATE);
                    break;
            }
        }
    }
}
