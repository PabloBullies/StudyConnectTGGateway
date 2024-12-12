package gigachadus.StudyConnect.service.repository;

import java.util.List;

public record SuggestMentorResponse(
        String id,
        String name,
        List<String>scientificInterests,
        List<DiplomaTopicResponse> diplomaTopics,
        String department
)  {

}
