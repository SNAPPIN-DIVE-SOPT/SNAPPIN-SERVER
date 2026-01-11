package org.sopt.snappinserver.domain.mood.service.dto.response;

import org.sopt.snappinserver.domain.mood.domain.entity.Mood;

public record GetMoodFilterResult(
    Long id,
    String category,
    String name,
    boolean isCurated
) {

    public static GetMoodFilterResult of(Mood mood, boolean isCurated) {
        return new GetMoodFilterResult(
            mood.getId(),
            mood.getCategory().getCategory(),
            mood.getName(),
            isCurated
        );
    }
}
