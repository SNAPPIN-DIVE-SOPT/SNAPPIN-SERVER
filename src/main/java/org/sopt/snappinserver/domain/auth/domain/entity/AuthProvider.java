package org.sopt.snappinserver.domain.auth.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.enums.SocialProvider;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_auth_provider_id",
        columnNames = {"social_provider", "provider_id"}
    )
})
public class AuthProvider extends BaseEntity {

    private static final int MAX_PROVIDER_ID_LENGTH = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_provider_seq_gen")
    @SequenceGenerator(
        name = "auth_provider_seq_gen",
        sequenceName = "auth_provider_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialProvider socialProvider;

    @Column(nullable = false, length = MAX_PROVIDER_ID_LENGTH)
    private String providerId;

    @Builder(access = AccessLevel.PRIVATE)
    private AuthProvider(User user, SocialProvider socialProvider, String providerId) {
        this.user = user;
        this.socialProvider = socialProvider;
        this.providerId = providerId;
    }

    public static AuthProvider create(User user, SocialProvider socialProvider, String providerId) {
        validateAuthProvider(user, socialProvider, providerId);
        return AuthProvider.builder()
            .user(user)
            .socialProvider(socialProvider)
            .providerId(providerId)
            .build();
    }

    private static void validateAuthProvider(
        User user,
        SocialProvider socialProvider,
        String providerId
    ) {
        validateUserExists(user);
        validateSocialProviderExists(socialProvider);
        validateProviderId(providerId);
    }

    private static void validateUserExists(User user) {
        if (user == null) {
            throw new AuthException(AuthErrorCode.USER_REQUIRED);
        }
    }

    private static void validateSocialProviderExists(SocialProvider socialProvider) {
        if (socialProvider == null) {
            throw new AuthException(AuthErrorCode.SOCIAL_PROVIDER_REQUIRED);
        }
    }

    private static void validateProviderId(String providerId) {
        validateProviderIdExists(providerId);
        validateProviderIdLength(providerId);
    }

    private static void validateProviderIdExists(String providerId) {
        if (providerId == null || providerId.isBlank()) {
            throw new AuthException(AuthErrorCode.PROVIDER_ID_REQUIRED);
        }
    }

    private static void validateProviderIdLength(String providerId) {
        if (providerId.length() > MAX_PROVIDER_ID_LENGTH) {
            throw new AuthException(AuthErrorCode.PROVIDER_ID_TOO_LONG);
        }
    }

}
