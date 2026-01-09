package org.sopt.snappinserver.domain.auth.service.dto.response;

import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;

public record LoginResult(String accessToken, String refreshToken) {

    public static LoginResult from(TokenPair tokenPair) {
        return new LoginResult(tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
