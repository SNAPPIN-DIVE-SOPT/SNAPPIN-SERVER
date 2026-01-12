package org.sopt.snappinserver.domain.mood.service.usecase;

import jakarta.annotation.Nullable;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterListResult;

public interface GetMoodFilterListUseCase {

    GetMoodFilterListResult getMoodFilters(@Nullable Long userId);

}
