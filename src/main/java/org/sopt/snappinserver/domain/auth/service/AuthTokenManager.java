package org.sopt.snappinserver.domain.auth.service;

import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.infra.redis.RefreshTokenStore;
import org.sopt.snappinserver.domain.auth.infra.redis.RefreshTokenValue;
import org.sopt.snappinserver.domain.auth.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.auth.jwt.JwtProvider;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthTokenManager {

    private final JwtProvider jwtProvider;
    private final RefreshTokenStore refreshTokenStore;

    @Value("${jwt.refresh-token-ttl-seconds}")
    private long refreshTokenSeconds;

    public LoginResult issueTokens(User user, String userAgent) {
        String accessToken = jwtProvider.createAccessToken(CustomUserInfo.from(user));
        String refreshToken = UUID.randomUUID().toString();

        saveRefreshToken(user.getId(), refreshToken, userAgent);

        return LoginResult.create(accessToken, refreshToken);
    }

    private void saveRefreshToken(Long userId, String refreshToken, String userAgent) {
        String userAgentHash =
            (userAgent == null) ? null : Integer.toHexString(userAgent.hashCode());
        Duration ttl = Duration.ofSeconds(refreshTokenSeconds);

        refreshTokenStore.save(
            refreshToken,
            new RefreshTokenValue(userId, userAgentHash),
            ttl
        );
    }
}
