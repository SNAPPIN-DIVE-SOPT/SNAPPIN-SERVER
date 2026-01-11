package org.sopt.snappinserver.domain.mood.service.dto.response;

import java.util.List;
import java.util.Set;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;

public record GetMoodFilterListResult(
    List<GetMoodFilterResult> moods
) {

    public static GetMoodFilterListResult from(
        List<Mood> moods,
        Set<Long> curatedMoodIds
    ) {
        List<GetMoodFilterResult> moodDtos = moods.stream()
            .map(mood -> GetMoodFilterResult.of(
                mood,
                curatedMoodIds.contains(mood.getId())
            ))
            .toList();

        return new GetMoodFilterListResult(moodDtos);
    }

}
