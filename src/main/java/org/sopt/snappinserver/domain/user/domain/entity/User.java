package org.sopt.snappinserver.domain.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.user.domain.enums.UserRole;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_PROFILE_IMAGE_URL_LENGTH = 1024;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
    @SequenceGenerator(
        name = "users_seq_gen",
        sequenceName = "users_seq",
        allocationSize = 1
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false, length = MAX_PROFILE_IMAGE_URL_LENGTH)
    private String profileImageUrl;

    @Builder(access = AccessLevel.PRIVATE)
    private User(UserRole role, String name, String profileImageUrl) {
        this.role = role;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public static User create(UserRole role, String name, String profileImageUrl) {
        validateUser(role, name, profileImageUrl);
        return User.builder()
            .role(role)
            .name(name)
            .profileImageUrl(profileImageUrl)
            .build();
    }

    private static void validateUser(UserRole role, String name, String profileImageUrl) {
        validateUserRoleExists(role);
        validateNameExists(name);
        validateNameLength(name);
    }

    private static void validateUserRoleExists(UserRole role) {
        if (role == null) {
            throw new UserException(UserErrorCode.USER_ROLE_REQUIRED);
        }
    }

    private static void validateNameExists(String name) {
        if (name == null || name.isBlank()) {
            throw new UserException(UserErrorCode.NAME_REQUIRED);
        }
    }

    private static void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new UserException(UserErrorCode.NAME_LENGTH_TOO_LONG);
        }
    }

    private static void validateProfileImageUrl(String profileImageUrl) {
        if(profileImageUrl == null || profileImageUrl.isBlank()) {

        }
        if(profileImageUrl.length() > MAX_PROFILE_IMAGE_URL_LENGTH) {

        }
    }
}
