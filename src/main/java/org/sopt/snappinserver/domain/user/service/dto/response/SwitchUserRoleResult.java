package org.sopt.snappinserver.domain.user.service.dto.response;

import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;

public record SwitchUserRoleResult(String accessToken, String refreshToken) {

    public static SwitchUserRoleResult from(TokenPair tokenPair) {
        return new SwitchUserRoleResult(tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
