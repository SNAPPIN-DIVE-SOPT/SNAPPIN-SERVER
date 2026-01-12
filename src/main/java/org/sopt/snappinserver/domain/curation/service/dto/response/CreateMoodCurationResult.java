package org.sopt.snappinserver.domain.curation.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.user.domain.entity.User;

public record CreateMoodCurationResult(
    String name,
    List<CreateMoodResult> moods
) {

    public static CreateMoodCurationResult of(User user, List<Mood> moods) {
        return new CreateMoodCurationResult(
            user.getName(),
            moods.stream().map(CreateMoodResult::from).toList()
        );
    }
}
