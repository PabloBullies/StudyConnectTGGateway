package gigachadus.StudyConnect.service.repository;

import java.util.List;

public record StudentResponseWithIsApprove(
        String id,

        String name,

        String email,

        String tgNickname,

        List<String> scientificInterests,

        List<String> skills,

        String department,

        String initiativeTheme,

        Boolean isApprove
) {
}