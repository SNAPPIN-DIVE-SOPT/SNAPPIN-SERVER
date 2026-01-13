package org.sopt.snappinserver.domain.user.service.dto.response;

import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.auth.service.dto.response.ReissueTokenResult;

public record SwitchRoleResult(String accessToken, String refreshToken) {

    public static ReissueTokenResult from(TokenPair tokenPair) {
        return new ReissueTokenResult(tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
