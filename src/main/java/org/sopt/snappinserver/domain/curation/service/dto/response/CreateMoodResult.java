package org.sopt.snappinserver.domain.curation.service.dto.response;

import org.sopt.snappinserver.domain.mood.domain.entity.Mood;

public record CreateMoodResult(
    Long id,
    String name
) {

    public static CreateMoodResult from(Mood mood) {
        return new CreateMoodResult(mood.getId(), mood.getName());
    }
}
