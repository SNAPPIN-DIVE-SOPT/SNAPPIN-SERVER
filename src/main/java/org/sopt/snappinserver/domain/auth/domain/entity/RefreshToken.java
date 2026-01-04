package org.sopt.snappinserver.domain.auth.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends BaseEntity {

    private static final int MAX_USER_AGENT_LENGTH = 512;
    private static final int MAX_TOKEN_LENGTH = 512;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = MAX_USER_AGENT_LENGTH)
    private String userAgent;

    @Column(nullable = false, length = MAX_TOKEN_LENGTH)
    private String refreshToken;

    @Column(nullable = false)
    private Instant expiresAt;

    @Builder(access = AccessLevel.PRIVATE)
    private RefreshToken(User user, String userAgent, String refreshToken, Instant expiresAt) {
        this.user = user;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public static RefreshToken create(
        User user,
        String userAgent,
        String refreshToken,
        Instant expiresAt
    ) {
        validateRefreshToken(user, refreshToken, userAgent, expiresAt);
        return RefreshToken.builder()
            .user(user)
            .refreshToken(refreshToken)
            .expiresAt(expiresAt)
            .build();
    }

    private static void validateRefreshToken(
        User user,
        String userAgent,
        String refreshToken,
        Instant expiresAt
    ) {
        validateUserExists(user);
        validateUserAgentLength(userAgent);
        validateRefreshTokenValue(refreshToken);
        validateExpiresAtExists(expiresAt);
    }

    private static void validateUserExists(User user) {
        if (user == null) {
            throw new AuthException(AuthErrorCode.USER_REQUIRED);
        }
    }

    private static void validateUserAgentLength(String userAgent) {
        if (userAgent.length() > MAX_USER_AGENT_LENGTH) {
            throw new AuthException(AuthErrorCode.USER_AGENT_LENGTH_TOO_LONG);
        }
    }

    private static void validateRefreshTokenValue(String refreshToken) {
        validateRefreshTokenValueExists(refreshToken);
        validateRefreshTokenValueLength(refreshToken);
    }

    private static void validateRefreshTokenValueExists(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_REQUIRED);
        }
    }

    private static void validateRefreshTokenValueLength(String refreshToken) {
        if (refreshToken.length() > MAX_TOKEN_LENGTH) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_TOO_LONG);
        }
    }

    private static void validateExpiresAtExists(Instant expiresAt) {
        if (expiresAt == null) {
            throw new AuthException(AuthErrorCode.EXPIRES_AT_REQUIRED);
        }
    }

}
