package gigachadus.StudyConnect.service.repository;

import java.util.List;

public record MentorResponseWithIsApprove(

        String id,

        String name,

        String email,

        String tgNickname,

        List<String> scientificInterests,

        List<DiplomaTopicResponse> diplomaTopics,

        String department,

        Boolean isApprove
) {
}