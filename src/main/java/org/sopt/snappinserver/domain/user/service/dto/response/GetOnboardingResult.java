package org.sopt.snappinserver.domain.user.service.dto.response;

import org.sopt.snappinserver.domain.user.domain.entity.Onboarding;

public record GetOnboardingResult(
    String name,
    String phoneNumber,
    String email
) {

    public static GetOnboardingResult create(Onboarding onboarding) {
        return new GetOnboardingResult(
            onboarding.getName(),
            onboarding.getPhoneNumber(),
            onboarding.getEmail()
        );
    }
}
