package org.sopt.snappinserver.domain.auth.service.dto.response;

import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;

public record LoginResult(boolean isNew, String accessToken, String refreshToken) {

    public static LoginResult of(boolean isNew, TokenPair tokenPair) {
        return new LoginResult(isNew, tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
