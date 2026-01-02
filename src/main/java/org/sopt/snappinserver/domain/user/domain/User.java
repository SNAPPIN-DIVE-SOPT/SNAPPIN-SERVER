package org.sopt.snappinserver.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Builder(access = AccessLevel.PRIVATE)
    private User(UserRole role, String name) {
        this.role = role;
        this.name = name;
    }

    public static User create(UserRole role, String name) {
        validateUser(role, name);
        return User.builder()
            .role(role)
            .name(name)
            .build();
    }

    private static void validateUser(UserRole role, String name) {
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
}
