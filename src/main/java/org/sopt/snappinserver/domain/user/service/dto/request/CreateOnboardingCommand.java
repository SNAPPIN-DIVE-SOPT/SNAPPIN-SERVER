package org.sopt.snappinserver.domain.user.service.dto.request;

import java.util.List;
import org.sopt.snappinserver.api.v1.user.dto.request.CreateOnboardingRequest;
import org.sopt.snappinserver.global.enums.Gender;
import org.sopt.snappinserver.global.enums.SnapCategory;

public record CreateOnboardingCommand(
    Long userId,
    String name,
    Gender gender,
    String nickname,
    String phoneNumber,
    String email,
    List<SnapCategory> snapCategories
) {

    public static CreateOnboardingCommand create(Long userId, CreateOnboardingRequest request) {
        return new CreateOnboardingCommand(
            userId,
            request.name(),
            request.gender(),
            request.nickname(),
            request.phoneNumber(),
            request.email(),
            List.copyOf(request.snapCategories())
        );
    }

}
