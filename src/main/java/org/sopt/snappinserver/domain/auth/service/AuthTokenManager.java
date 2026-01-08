package org.sopt.snappinserver.domain.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.auth.infra.redis.RefreshTokenStore;
import org.sopt.snappinserver.domain.auth.infra.redis.RefreshTokenValue;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.auth.infra.jwt.JwtProvider;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthTokenManager {

    private static final String HASH_ALGORITHM = "SHA-256";

    private final JwtProvider jwtProvider;
    private final RefreshTokenStore refreshTokenStore;

    @Value("${jwt.refresh-token-ttl-seconds}")
    private long refreshTokenSeconds;

    public TokenPair issueTokenPair(User user, String userAgent) {
        String accessToken = jwtProvider.createAccessToken(CustomUserInfo.from(user));
        String refreshToken = UUID.randomUUID().toString();

        saveRefreshToken(user.getId(), refreshToken, userAgent);

        return new TokenPair(accessToken, refreshToken);
    }

    private void saveRefreshToken(Long userId, String refreshToken, String userAgent) {
        String userAgentHash = (userAgent == null) ? null : hashUserAgent(userAgent);
        Duration ttl = Duration.ofSeconds(refreshTokenSeconds);

        refreshTokenStore.save(
            refreshToken,
            new RefreshTokenValue(userId, userAgentHash),
            ttl
        );
    }

    private String hashUserAgent(String userAgent) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(userAgent.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthException(AuthErrorCode.SHA_256_UNSUPPORTED);
        }
    }
}
