package org.sopt.snappinserver.domain.user.service.dto.response;

import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.user.domain.enums.UserRole;

public record SwitchUserRoleResult(String accessToken, String refreshToken, String role) {

    public static SwitchUserRoleResult from(TokenPair tokenPair, UserRole role) {
        return new SwitchUserRoleResult(
            tokenPair.accessToken(),
            tokenPair.refreshToken(),
            role.name()
        );
    }
}
