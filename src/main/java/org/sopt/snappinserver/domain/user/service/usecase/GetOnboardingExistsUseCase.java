package org.sopt.snappinserver.domain.user.service.usecase;

import org.sopt.snappinserver.domain.user.service.dto.response.GetOnboardingExistsResult;

public interface GetOnboardingExistsUseCase {

    GetOnboardingExistsResult hasOnboarding(Long userId);
}
