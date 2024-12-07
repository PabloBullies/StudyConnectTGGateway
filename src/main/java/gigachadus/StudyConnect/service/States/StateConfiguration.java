package gigachadus.StudyConnect.service.States;

import gigachadus.StudyConnect.config.BotConfig;
import gigachadus.StudyConnect.service.States.StudentRegistration.InitiativeThemeState;
import gigachadus.StudyConnect.service.States.StudentRegistration.SkillsRegistrationState;
import gigachadus.StudyConnect.service.States.StudentRegistration.StudentMainState;
import gigachadus.StudyConnect.service.States.TeacherRegistration.DeleteThemeState;
import gigachadus.StudyConnect.service.States.TeacherRegistration.DiplomaTopicsState;
import gigachadus.StudyConnect.service.States.TeacherRegistration.TeacherMainState;
import gigachadus.StudyConnect.service.States.TeacherRegistration.ThemesRegistration.NeededSkillsNameState;
import gigachadus.StudyConnect.service.States.TeacherRegistration.ThemesRegistration.ScientificFieldState;
import gigachadus.StudyConnect.service.States.TeacherRegistration.ThemesRegistration.SummaryState;
import gigachadus.StudyConnect.service.States.TeacherRegistration.ThemesRegistration.ThemeNameState;
import gigachadus.StudyConnect.service.repository.DataSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static java.util.Map.entry;

@Configuration
public class StateConfiguration {
    public static final String START_STATE = "START_STATE";
    public static final String INIT_STATE = "INIT_STATE";
    public static final String TEACHER_OR_STUDENT_STATE = "TEACHER_OR_STUDENT_STATE";
    public static final String NAME_REGISTRATION_STATE = "NAME_REGISTRATION_STATE";
    public static final String EMAIL_REGISTRATION_STATE = "EMAIL_REGISTRATION_STATE";
    public static final String TG_NICKNAME = "TG_NICKNAME";
    public static final String DEPARTMENT_REGISTRATION_STATE = "DEPARTMENT_REGISTRATION_STATE";
    public static final String FINISH_REGISTRATION_STATE = "FINISH_REGISTRATION_STATE";

    public static final String STUDENT_MAIN_STATE = "STUDENT_MAIN_STATE";
    public static final String INTERESTS_STATE = "INTERESTS_STATE";
    public static final String STUD_SKILLS_STATE = "STUD_SKILLS_STATE";
    public static final String STUD_INIT_THEME_STATE = "STUD_INIT_THEME_STATE";

    public static final String TEACHER_MAIN_STATE = "TEACHER_MAIN_STATE";


    public static final String DIPLOMA_TOPICS_STATE = "DIPLOMA_TOPICS_STATE";
    public static final String THEME_NAME_STATE = "THEME_NAME_STATE";
    public static final String THEME_NEEDED_SKILLS_STATE = "THEME_NEEDED_SKILLS_STATE";
    public static final String THEME_SCIENTIFIC_FIELDS_STATE = "THEM_SCIENTIFIC_FIELDS_STATE";
    public static final String THEME_SUMMARY_STATE = "THEME_SUMMARY_STATE";
    public static final String DELETE_TOPIC_STATE = "DELETE_TOPIC_STATE";



    public static final Map<String, String> allScientificFields = Map.ofEntries(
            entry("Математика", "/math"),
            entry("Физика", "/physics"),
            entry("Программирование", "/programming"),
            entry("Химия", "/chemistry"),
            entry("Биология", "/biology"),
            entry("Медицина", "/medicine"),
            entry("Искусственный интеллект", "/ai"),
            entry("Экономика", "/economics"),
            entry("История", "/history"),
            entry("Философия", "/philosophy"),
            entry("Литература", "/literature"),
            entry("Социология", "/sociology"),
            entry("Экология", "/ecology"),
            entry("География", "/geography"),
            entry("Информатика", "/informatics")
    );



    public static final Map<String, String> allSkillTag = Map.ofEntries(
            entry("Ответственность", "/responsibility"),
            entry("Командная работа", "/teamwork"),
            entry("Лидерство", "/leadership"),
            entry("Коммуникабельность", "/communication"),
            entry("Аналитическое мышление", "/analytical_thinking"),
            entry("Креативность", "/creativity"),
            entry("Решение проблем", "/problem_solving"),
            entry("Гибкость", "/adaptability"),
            entry("Мотивация", "/motivation"),
            entry("Навыки тайм-менеджмента", "/time_management"),
            entry("Эмоциональный интеллект", "/emotional_intelligence"),
            entry("Инициативность", "/initiative"),
            entry("Навыки переговоров", "/negotiation_skills"),
            entry("Управление проектами", "/project_management"),
            entry("Навыки презентации", "/presentation_skills"),
            entry("Техническая грамотность", "/technical_literacy")
    );

    public static String getKey(Map<String, String> map, String val){
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(val)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Bean(name = FINISH_REGISTRATION_STATE)
    public State finish_registration_state(){
        return new FinishRegistrationState();
    }

    @Bean(name = DELETE_TOPIC_STATE)
    public State delete_topic_state(){
        return new DeleteThemeState();
    }

    @Bean(name = THEME_SUMMARY_STATE)
    public State theme_summaru_state(){
        return new SummaryState();
    }

    @Bean(name = THEME_SCIENTIFIC_FIELDS_STATE)
    public State theme_scientific_fields_state(){
        return new ScientificFieldState();
    }

    @Bean(name = THEME_NAME_STATE)
    public State diploma_name_state(){
        return new ThemeNameState();
    }

    @Bean(name = THEME_NEEDED_SKILLS_STATE)
    public State theme_needed_state(){
        return new NeededSkillsNameState();
    }

    @Bean(name = STUDENT_MAIN_STATE)
    public State student_main_state(){
        return new StudentMainState();
    }

    @Bean(name = TEACHER_MAIN_STATE)
    public State teacher_main_state(){
        return new TeacherMainState();
    }


    @Bean(name = DIPLOMA_TOPICS_STATE)
    public State diploma_topics_state(){
        return new DiplomaTopicsState();
    }

    @Bean(name = STUD_INIT_THEME_STATE)
    public State stud_init_theme_state(){
        return new InitiativeThemeState();
    }

    @Bean(name = STUD_SKILLS_STATE)
    public State stud_skills_state() {
        return new SkillsRegistrationState();
    }

    @Bean(name = INTERESTS_STATE)
    public State interests_state() {
        return new InterestsRegisrtationState();
    }

    @Bean(name = DEPARTMENT_REGISTRATION_STATE)
    public State depart_registration_state() {
        return new DepartmentRegistrationState();
    }

    @Bean(name = EMAIL_REGISTRATION_STATE)
    public State email_registration_state() {
        return new EmailRegistrationState();
    }

    @Bean(name = NAME_REGISTRATION_STATE)
    public State name_registration_state() {
        return new NameRegistrationState();
    }

    @Bean(name = TEACHER_OR_STUDENT_STATE)
    public State teacher_or_student_state() {
        return new TeacherOrStudentState();
    }

    @Bean(name = START_STATE)
    public State startState(){
        return new StartState();
    }

    @Bean(name = INIT_STATE)
    public State initState(){
        return new InitState();
    }


}
