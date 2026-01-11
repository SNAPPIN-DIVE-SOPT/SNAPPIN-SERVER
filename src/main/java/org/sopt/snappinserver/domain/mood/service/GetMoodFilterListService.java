package org.sopt.snappinserver.domain.mood.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.curation.repository.CurationRepositoryCustom;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterListResult;
import org.sopt.snappinserver.domain.mood.service.usecase.GetMoodFilterListUseCase;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetMoodFilterListService implements GetMoodFilterListUseCase {

    private final CurationRepositoryCustom curationRepository;
    private final MoodRepository moodRepository;

    @Override
    public GetMoodFilterListResult getMoodFilters(Long userId) {
        List<Mood> moods = moodRepository.findAll();

        if (userId == null) {
            return GetMoodFilterListResult.from(moods, Set.of());
        }

        List<Long> top3MoodIds = curationRepository.findTop3MoodIdsByUserId(userId);

        return GetMoodFilterListResult.from(moods, new HashSet<>(top3MoodIds));
    }
}
