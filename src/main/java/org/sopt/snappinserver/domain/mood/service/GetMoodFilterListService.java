package org.sopt.snappinserver.domain.mood.service;

import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.curation.repository.CurationRepositoryCustom;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterListResult;
import org.sopt.snappinserver.domain.mood.service.usecase.GetMoodFilterListUseCase;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetMoodFilterListService implements GetMoodFilterListUseCase {

    private final CurationRepositoryCustom curationRepository;
    private final MoodRepository moodRepository;

    @Override
    public GetMoodFilterListResult getMoodFilters(@Nullable Long userId) {
        List<Mood> moods = moodRepository.findAll(Sort.by("id"));
        if (userId == null) {
            return GetMoodFilterListResult.from(moods, Set.of());
        }

        List<Long> top3MoodIds = curationRepository.findTop3MoodIdsByUserId(userId);

        return GetMoodFilterListResult.from(moods, Set.copyOf(top3MoodIds));
    }
}
