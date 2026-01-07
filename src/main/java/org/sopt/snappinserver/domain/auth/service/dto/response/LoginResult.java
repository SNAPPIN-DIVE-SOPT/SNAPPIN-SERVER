package org.sopt.snappinserver.domain.auth.service.dto.response;

public record LoginResult(String accessToken, String refreshToken) {

    public static LoginResult create(String accessToken, String refreshToken) {
        return new LoginResult(accessToken, refreshToken);
    }
}
