package org.sopt.snappinserver.domain.auth.service.dto.response;

import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerExistenceResult;

public record LoginWithPhotographerProfileResult(
    boolean isNew,
    String accessToken,
    String refreshToken,
    String role,
    boolean hasPhotographerProfile
) {

    public static LoginWithPhotographerProfileResult of(
        LoginResult loginResult,
        GetPhotographerExistenceResult getPhotographerExistenceResult
    ) {
        return new LoginWithPhotographerProfileResult(
            loginResult.isNew(),
            loginResult.accessToken(),
            loginResult.refreshToken(),
            loginResult.role(),
            getPhotographerExistenceResult.hasPhotographerProfile()
        );
    }
}
