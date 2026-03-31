package org.sopt.snappinserver.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.user.domain.entity.Onboarding;
import org.sopt.snappinserver.domain.user.domain.entity.OnboardingSnapCategory;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.OnboardingRepository;
import org.sopt.snappinserver.domain.user.repository.OnboardingSnapCategoryRepository;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.user.service.dto.request.CreateOnboardingCommand;
import org.sopt.snappinserver.domain.user.service.usecase.CreateUserOnboardingUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CreateUserOnboardingService implements CreateUserOnboardingUseCase {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final OnboardingSnapCategoryRepository onboardingSnapCategoryRepository;

    @Override
    public void createUserOnboarding(CreateOnboardingCommand command) {
        User user = getExistingUser(command.userId());
        Onboarding onboarding = Onboarding.create(
            user,
            command.name(),
            command.gender(),
            command.nickname(),
            command.phoneNumber(),
            command.email()
        );
        onboardingRepository.save(onboarding);

        List<OnboardingSnapCategory> categories = command.snapCategories().stream()
            .map(category -> OnboardingSnapCategory.create(onboarding, category))
            .toList();
        onboardingSnapCategoryRepository.saveAll(categories);
    }

    private User getExistingUser(Long userID) {
        return userRepository.findById(userID)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
