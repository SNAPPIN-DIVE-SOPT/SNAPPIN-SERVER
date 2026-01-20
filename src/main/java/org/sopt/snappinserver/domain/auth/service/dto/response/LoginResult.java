package org.sopt.snappinserver.domain.auth.service.dto.response;

import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.user.domain.enums.UserRole;

public record LoginResult(
    boolean isNew,
    String accessToken,
    String refreshToken,
    String role
) {

    public static LoginResult of(boolean isNew, TokenPair tokenPair, UserRole userRole) {
        return new LoginResult(
            isNew,
            tokenPair.accessToken(),
            tokenPair.refreshToken(),
            userRole.name()
        );
    }
}
