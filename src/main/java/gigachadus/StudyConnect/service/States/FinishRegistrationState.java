package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.service.TelegramBot;
import gigachadus.StudyConnect.service.repository.DataSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

public class FinishRegistrationState implements State {

    @Override
    public void questionState(Long chatId, TelegramBot telegramBot) {
        telegramBot.sendMessage(chatId, "Проверка корректности данных:");
        Map<String, Object> data = telegramBot.getUserEnteredData(chatId);


        try {
            Boolean is_student = (Boolean) data.get(StateConfiguration.TEACHER_OR_STUDENT_STATE);
            if (is_student){
                HttpResponse<String> response = telegramBot.getDataSender().createStudent(data);

                if (response.statusCode() == 200){
                    telegramBot.sendMessage(chatId, "Профиль  студента успешно зарегистрирован!");
                    Map<String, Object> responseMap = telegramBot.getDataSender().jsonToMap(response.body());
                    data.put("BD_ID", responseMap.get("id"));
                    data.put("BD_PAGE", 0);
                    telegramBot.setCurrentState(chatId, StateConfiguration.STUDENT_MAIN_STATE);
                }else{
                    telegramBot.sendMessage(chatId, response.body());
                    telegramBot.setCurrentState(chatId, StateConfiguration.NAME_REGISTRATION_STATE);
                }
            } else{
                HttpResponse<String> response = telegramBot.getDataSender().createMentor(data);
                if (response.statusCode() == 200){
                    telegramBot.sendMessage(chatId, "Профиль преподавателя успешно зарегистрирован!");
                    Map<String, Object> responseMap = telegramBot.getDataSender().jsonToMap(response.body());
                    data.put("BD_ID", responseMap.get("id"));
                    data.put("BD_PAGE", 0);
                    telegramBot.setCurrentState(chatId, StateConfiguration.TEACHER_MAIN_STATE);
                }else{
                    telegramBot.sendMessage(chatId, response.body());
                    telegramBot.setCurrentState(chatId, StateConfiguration.NAME_REGISTRATION_STATE);
                }
            }


        } catch (InterruptedException e) {
            System.err.println(e);
            telegramBot.clearUsersData(chatId);
            telegramBot.setCurrentState(chatId, StateConfiguration.INIT_STATE);

        } catch (IOException e) {
            System.err.println(e);
            telegramBot.clearUsersData(chatId);
            telegramBot.setCurrentState(chatId, StateConfiguration.INIT_STATE);
        }

    }

    @Override
    public void answerState(Long chatId, Update update, TelegramBot telegramBot) {

    }
}
