package gigachadus.StudyConnect.service.repository;

import com.google.gson.reflect.TypeToken;
import gigachadus.StudyConnect.config.BotConfig;
import gigachadus.StudyConnect.service.States.StateConfiguration;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import org.stringtemplate.v4.ST;
import gigachadus.StudyConnect.service.States.StateConfiguration;
import org.springframework.beans.factory.annotation.Value;

import static java.util.stream.Collectors.toList;


public class DataSender {

//    ST createTopicSt = new ST("""
//            {
//              "name": "<name>",
//              "summary": "<summary>",
//              "neededSkills": <skills>,
//              "scientificField": "<field>"
//            }
//            """);


    private final BotConfig botConfig;

    public DataSender(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    public HttpResponse<String> createStudent(Map<String, Object> data) throws IOException, InterruptedException {

        ST createStudentSt = new ST("""
            {
              "name": "<name>",
              "email": "<email>",
              "tgNickname": "<tgNick>",
              "scientificInterests": <interests>,
              "department": "<depart>",
              "skills": <skills>,
              "initiativeTheme": "<init_theme>"
            }
            """);


        String name = (String) data.get(StateConfiguration.NAME_REGISTRATION_STATE);
        String email = (String) data.get(StateConfiguration.EMAIL_REGISTRATION_STATE);
        String tgNickname = (String) data.get(StateConfiguration.TG_NICKNAME);
        List<String> interestsList = ((Set<String>) data.get(StateConfiguration.INTERESTS_STATE)).stream().toList();
        List<String> skillsList = ((Set<String>) data.get(StateConfiguration.STUD_SKILLS_STATE)).stream().toList();
        String depart = (String) data.get(StateConfiguration.DEPARTMENT_REGISTRATION_STATE);
        String init_theme = (String) data.get(StateConfiguration.STUD_INIT_THEME_STATE);

        Gson gson = new Gson();

        String jsonBody = createStudentSt
                .add("name", name)
                .add("email", email)
                .add("tgNick", tgNickname)
                .add("interests", gson.toJson(interestsList))
                .add("skills", gson.toJson(skillsList))
                .add("depart", depart)
                .add("init_theme", init_theme)
                .render();


//        List<String> interestsList = List.of("/interes1", "/interes2", "/interes3");
//        String interestsString = gson.toJson(interestsList);
//
//        List<String> skillsList = List.of("/skill1", "/skill2", "/skill3");
//        String skillsString = gson.toJson(interestsList);
//
//        String jsonBody = createStudentSt
//                .add("name", "Dima")
//                .add("email", "mail@gmail.com")
//                .add("tgNick", "@MDm1try")
//                .add("interests", interestsString)
//                .add("skills", skillsString)
//                .add("depart", "/FF")
//                .add("init_theme", "123")
//                .render();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri((URI.create("https://" + botConfig.getMasterIp() + "/profiles/students")))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
        // Send the request and get the response
        System.out.println("Student request");
        System.out.println(jsonBody);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Student response");
        System.out.println(response.body());
        return response;
    }

    public HttpResponse<String> createMentor(Map<String, Object> data) throws IOException, InterruptedException {

        ST createMentorSt = new ST("""
            {
              "name": "<name>",
              "email": "<email>",
              "tgNickname": "<tgNick>",
              "department": "<depart>",
              "scientificInterests": <interests>,
              "diplomaTopics": <topics>
            }
            """);


        String name = (String) data.get(StateConfiguration.NAME_REGISTRATION_STATE);
        String email = (String) data.get(StateConfiguration.EMAIL_REGISTRATION_STATE);
        String tgNickname = (String) data.get(StateConfiguration.TG_NICKNAME);
        List<String> interestsList = ((Set<String>) data.get(StateConfiguration.INTERESTS_STATE)).stream().toList();
        String depart = (String) data.get(StateConfiguration.DEPARTMENT_REGISTRATION_STATE);
        List<Map<String, Object>> topics = (List<Map<String, Object>>) data.getOrDefault("DIPLOMA_TOPICS", new ArrayList<Map<String, Object>>());

        String readyMentor = "{TG_NICKNAME=@MDm1try, DIPLOMA_TOPICS=[{THEME_NEEDED_SKILLS_STATE=[/public_speaking, /problem_solving], THEME_NAME_STATE=theme1, THEME_SUMMARY_STATE=123, THEM_SCIENTIFIC_FIELDS_STATE=filed}], EMAIL_REGISTRATION_STATE=mail@gmail.com, INTERESTS_STATE=[/management, /machine_learning, /philosophy, /programming], NAME_REGISTRATION_STATE=Mentor3, DEPARTMENT_REGISTRATION_STATE=/MMF, TEACHER_OR_STUDENT_STATE=false, CURRENT_THEME_NAME=theme1}";

        Gson gson = new Gson();
        List<Map<String, Object>> topics_show = topics.stream()
                .map(t -> {
                    Map<String, Object> newMap = new HashMap<>();
                    newMap.put("name", t.get(StateConfiguration.THEME_NAME_STATE));
                    newMap.put("summary", t.get(StateConfiguration.THEME_SUMMARY_STATE));
                    List<String> needed_skills_list = ((Set<String>) t.get(StateConfiguration.THEME_NEEDED_SKILLS_STATE)).stream().toList();
                    // String needed_skills = needed_skills_set; // gson.toJson?
                    newMap.put("neededSkills", needed_skills_list);
                    newMap.put("scientificField", t.get(StateConfiguration.THEME_SCIENTIFIC_FIELDS_STATE));
                    return newMap;

                })
                .collect(Collectors.toList());


        String jsonBody = createMentorSt
                .add("name", name)
                .add("email", email)
                .add("tgNick", tgNickname)
                .add("interests", gson.toJson(interestsList))
                .add("depart", depart)
                .add("topics", gson.toJson(topics_show))
                .render();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri((URI.create("https://" + botConfig.getMasterIp() + "/profiles/mentors")))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();
        System.out.println("Mentor request");
        System.out.println(jsonBody);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Mentor response");
        System.out.println(response.body());
        return response;
    }

    public Map<String, Object> jsonToMap(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map = gson.fromJson(str, type);

        System.out.println(map);
        return map;
    }

    public HttpResponse<String> getMentorSuggestion(String bd_id, Integer page) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("https://%s/suggest/%s/mentors?skip=%d",
                botConfig.getMasterIp(), bd_id, page);
        HttpRequest request = HttpRequest.newBuilder()
                .uri((URI.create(url)))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> matchMentor(String studentId, String mentorId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("https://%s/matches/%s/mentors/%s",
                botConfig.getMasterIp(), studentId, mentorId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri((URI.create(url)))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> matchStudent(String mentorId, String studentId, Boolean isApprove) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("https://%s/matches/%s/mentors/%s?isApprove=%s",
                botConfig.getMasterIp(), mentorId, studentId, isApprove);
        HttpRequest request = HttpRequest.newBuilder()
                .uri((URI.create(url)))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public HttpResponse<String> getMatchingStudents(String mentorId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("https://%s/matches/%s/students",
                botConfig.getMasterIp(), mentorId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri((URI.create(url)))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public HttpResponse<String> getMatchingMentors(String studentId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("https://%s/matches/%s/mentors",
                botConfig.getMasterIp(), studentId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri((URI.create(url)))
                .headers("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}