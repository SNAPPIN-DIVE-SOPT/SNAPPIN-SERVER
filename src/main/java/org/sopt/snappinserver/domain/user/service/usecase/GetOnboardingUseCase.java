package org.sopt.snappinserver.domain.user.service.usecase;

import org.sopt.snappinserver.domain.user.service.dto.response.GetOnboardingResult;

public interface GetOnboardingUseCase {

    GetOnboardingResult getOnboarding(Long userId);
}
