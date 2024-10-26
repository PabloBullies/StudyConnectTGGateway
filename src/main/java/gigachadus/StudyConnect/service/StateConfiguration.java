package gigachadus.StudyConnect.service;

import gigachadus.StudyConnect.service.States.InitState;
import gigachadus.StudyConnect.service.States.RegistrationState;
import gigachadus.StudyConnect.service.States.StartState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StateConfiguration {
    public static final String REGISTRATION_STATE = "REGISTRATION_STATE";
    public static final String START_STATE = "START_STATE";
    public static final String INIT_STATE = "INIT_STATE";

    @Bean(name = REGISTRATION_STATE)
    public State registrationState() {
        return new RegistrationState();
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
