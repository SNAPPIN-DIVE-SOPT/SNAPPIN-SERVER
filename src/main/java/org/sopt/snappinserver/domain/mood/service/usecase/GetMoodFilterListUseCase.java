package org.sopt.snappinserver.domain.mood.service.usecase;

import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterListResult;

public interface GetMoodFilterListUseCase {

    GetMoodFilterListResult getMoodFilters(Long userId);

}
