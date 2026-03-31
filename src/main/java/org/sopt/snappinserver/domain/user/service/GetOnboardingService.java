package org.sopt.snappinserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.user.domain.entity.Onboarding;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.OnboardingRepository;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.user.service.dto.response.GetOnboardingResult;
import org.sopt.snappinserver.domain.user.service.usecase.GetOnboardingUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetOnboardingService implements GetOnboardingUseCase {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;

    @Override
    public GetOnboardingResult getOnboarding(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        Onboarding onboarding = onboardingRepository.findByUser(user)
            .orElseThrow(() -> new UserException(UserErrorCode.ONBOARDING_NOT_FOUND));

        return GetOnboardingResult.create(onboarding);
    }

}
