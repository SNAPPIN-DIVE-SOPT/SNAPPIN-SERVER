package org.sopt.snappinserver.domain.user.service.dto.response;

import org.sopt.snappinserver.domain.user.domain.entity.Onboarding;
import org.sopt.snappinserver.global.enums.Gender;

public record GetOnboardingResult(
    String name,
    String phoneNumber,
    String email,
    Gender gender
) {

    public static GetOnboardingResult create(Onboarding onboarding) {
        return new GetOnboardingResult(
            onboarding.getName(),
            onboarding.getPhoneNumber(),
            onboarding.getEmail(),
            onboarding.getGender()
        );
    }
}
