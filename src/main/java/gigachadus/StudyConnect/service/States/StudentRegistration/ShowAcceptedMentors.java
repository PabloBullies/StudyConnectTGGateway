package gigachadus.StudyConnect.service.States.StudentRegistration;

import gigachadus.StudyConnect.service.States.State;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import gigachadus.StudyConnect.service.States.TeacherRegistration.DiplomaTopicsState;
import gigachadus.StudyConnect.service.TelegramBot;
import gigachadus.StudyConnect.service.repository.DiplomaTopicResponse;
import gigachadus.StudyConnect.service.repository.MentorResponseWithIsApprove;
import gigachadus.StudyConnect.service.repository.StudentResponseWithIsApprove;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowAcceptedMentors implements State {
    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        Map<String, Object> data = telegramBot.getUserEnteredData(chatId);
        List<MentorResponseWithIsApprove> acceptedMentors = (List<MentorResponseWithIsApprove>) data.get(StateConfiguration.SHOW_ACCEPTED_MENTORS_STATE);
        if (acceptedMentors.isEmpty()){
            telegramBot.sendMessage(chatId, "Согласившихся нет. Рекомендуем повторить поиск позже.");
            telegramBot.setCurrentState(chatId, StateConfiguration.STUDENT_MAIN_STATE);

        }else{
            telegramBot.sendMessage(chatId, "Поздравляю! У вас есть следующие matches. Начинайте общаться❤!\nPS не бойся писать первым\uD83D\uDE09)");
            for (MentorResponseWithIsApprove mentor : acceptedMentors){
                suggestMentor(chatId, telegramBot, mentor);
            }
            telegramBot.setCurrentState(chatId, StateConfiguration.STUDENT_MAIN_STATE);
        }

    }

    private void suggestMentor(Long chatId, TelegramBot telegramBot, MentorResponseWithIsApprove mentor){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Имя: ").append(mentor.name()).append("\n");
        stringBuilder.append("email: ").append(mentor.email()).append("\n");
        stringBuilder.append("tg: ").append(mentor.tgNickname()).append("\n");
        stringBuilder.append(mentor.department()).append("\n");
        stringBuilder.append("Интересы: ").append(mentor.scientificInterests()).append("\n");
        for(DiplomaTopicResponse topic : mentor.diplomaTopics()){
            stringBuilder.append("  Тема: ").append(topic.name()).append("\n");
            stringBuilder.append("  Комметарий: ").append(topic.summary()).append("\n");
        }

        String mentorString = stringBuilder.toString();
        telegramBot.sendMessage(chatId, mentorString);
    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {

    }
}
