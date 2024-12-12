package gigachadus.StudyConnect.service.repository;

import java.util.List;

public record DiplomaTopicResponse(
        String id,

        String name,

        String summary,

        List<String> neededSkills,

        String scientificField
) {
}
