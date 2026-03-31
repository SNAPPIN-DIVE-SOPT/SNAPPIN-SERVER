package org.sopt.snappinserver.domain.user.service.usecase;

import org.sopt.snappinserver.domain.user.service.dto.request.CreateOnboardingCommand;

public interface CreateUserOnboardingUseCase {

    void createUserOnboarding(CreateOnboardingCommand command);
}
