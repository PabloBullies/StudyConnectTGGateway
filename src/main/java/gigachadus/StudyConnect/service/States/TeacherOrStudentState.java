package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

public class TeacherOrStudentState implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Являешься студентом (/student) или преподавателем (/teacher)?");
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
            if (text.equalsIgnoreCase("/student")) {
                data.put(StateConfiguration.TEACHER_OR_STUDENT_STATE, true);
                telegramBot.sendMessage(chatId, "Начнем регистрацию для студента. Введи следующие данные:");
                telegramBot.setCurrentState(chatId, StateConfiguration.STUD_SKILLS_STATE);
            } if (text.equalsIgnoreCase("/teacher")) {
                data.put(StateConfiguration.TEACHER_OR_STUDENT_STATE, false);
                telegramBot.sendMessage(chatId, "Начнем регистрацию для преподвавателя. Введите следующие данные:");
                telegramBot.setCurrentState(chatId, StateConfiguration.DIPLOMA_TOPICS_STATE);
            }
        }
    }
}
