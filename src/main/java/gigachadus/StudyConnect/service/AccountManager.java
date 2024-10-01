package gigachadus.StudyConnect.service;

import org.springframework.stereotype.Component;

@Component
public class AccountManager {
    public String greeting(){
        String greeting_message = "Welcome to out monastery";
        String teacher_students_question = "Are you jedi or you want find your master?";
        return greeting_message + "\n" + teacher_students_question;
    }
}
