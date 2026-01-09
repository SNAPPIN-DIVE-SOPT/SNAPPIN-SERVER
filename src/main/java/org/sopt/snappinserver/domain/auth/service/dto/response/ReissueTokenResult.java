package org.sopt.snappinserver.domain.auth.service.dto.response;

import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;

public record ReissueTokenResult(String accessToken, String refreshToken) {

    public static ReissueTokenResult from(TokenPair tokenPair) {
        return new ReissueTokenResult(tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
