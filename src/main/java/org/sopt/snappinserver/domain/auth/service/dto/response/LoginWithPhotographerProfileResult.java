package org.sopt.snappinserver.domain.auth.service.dto.response;

import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerExistenceResult;
import org.sopt.snappinserver.domain.user.service.dto.response.GetOnboardingExistsResult;

public record LoginWithPhotographerProfileResult(
    boolean isNew,
    String accessToken,
    String refreshToken,
    String role,
    boolean hasPhotographerProfile,
    boolean isOnboardingCompleted
) {

    public static LoginWithPhotographerProfileResult of(
        LoginResult loginResult,
        GetPhotographerExistenceResult getPhotographerExistenceResult,
        GetOnboardingExistsResult getOnboardingExistsResult
    ) {
        return new LoginWithPhotographerProfileResult(
            loginResult.isNew(),
            loginResult.accessToken(),
            loginResult.refreshToken(),
            loginResult.role(),
            getPhotographerExistenceResult.hasPhotographerProfile(),
            getOnboardingExistsResult.isOnboardingCompleted()
        );
    }
}
