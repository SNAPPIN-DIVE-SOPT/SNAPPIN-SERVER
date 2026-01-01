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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Builder(access = AccessLevel.PRIVATE)
    private User(UserRole role, Gender gender) {
        this.role = role;
        this.gender = gender;
    }

    public static User create(UserRole role, Gender gender) {
        return User.builder()
            .role(role)
            .gender(gender)
            .build();
    }

    public void validateUser(UserRole role, Gender gender) {
        if (role == null) {
            throw new UserException(UserErrorCode.USER_ROLE_REQUIRED);
        }
        if (gender == null) {
            throw new UserException(UserErrorCode.GENDER_REQUIRED);
        }
    }
}
