package org.sopt.snappinserver.domain.auth.service.dto.response;

import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.user.domain.entity.User;

public record LoginResult(
    boolean isNew,
    String accessToken,
    String refreshToken,
    String role,
    Long userId
) {

    public static LoginResult of(boolean isNew, TokenPair tokenPair, User user) {
        return new LoginResult(
            isNew,
            tokenPair.accessToken(),
            tokenPair.refreshToken(),
            user.getRole().name(),
            user.getId()
        );
    }
}
