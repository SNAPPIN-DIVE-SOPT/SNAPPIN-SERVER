package org.sopt.snappinserver.domain.photographer.domain.entity;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerErrorCode;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerException;
import org.sopt.snappinserver.global.enums.Gender;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.global.entity.BaseEntity;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Photographer extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 10;
    private static final int MAX_NICKNAME_LENGTH = 20;
    private static final int MAX_BIO_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnapCategory specialty;

    @Column(length = MAX_BIO_LENGTH)
    private String bio;

    @Builder(access = AccessLevel.PRIVATE)
    private Photographer(
        User user,
        String name,
        String nickname,
        Gender gender,
        SnapCategory specialty,
        String bio
    ) {
        this.user = user;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.specialty = specialty;
        this.bio = bio;
    }

    public static Photographer create(
        User user,
        String name,
        String nickname,
        Gender gender,
        SnapCategory specialty,
        String bio
    ) {
        validatePhotographer(user, name, nickname, gender, specialty, bio);
        return Photographer.builder()
            .user(user)
            .name(name)
            .nickname(nickname)
            .gender(gender)
            .specialty(specialty)
            .bio(bio)
            .build();
    }

    private static void validatePhotographer(
        User user,
        String name,
        String nickname,
        Gender gender,
        SnapCategory specialty,
        String bio
    ) {
        validateUserExists(user);
        validateName(name);
        validateNickname(nickname);
        validateGenderExists(gender);
        validateSpecialtyExists(specialty);
        validateBioLength(bio);
    }

    private static void validateUserExists(User user) {
        if (user == null) {
            throw new PhotographerException(PhotographerErrorCode.USER_REQUIRED);
        }
    }

    private static void validateName(String name) {
        validateNameExists(name);
        validateNameLength(name);
    }

    private static void validateNameExists(String name) {
        if (name == null || name.isBlank()) {
            throw new PhotographerException(PhotographerErrorCode.NAME_REQUIRED);
        }
    }

    private static void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new PhotographerException(PhotographerErrorCode.NAME_LENGTH_TOO_LONG);
        }
    }

    private static void validateNickname(String nickname) {
        validateNicknameExists(nickname);
        validateNicknameLength(nickname);
    }

    private static void validateNicknameExists(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new PhotographerException(PhotographerErrorCode.NICKNAME_REQUIRED);
        }
    }

    private static void validateNicknameLength(String nickname) {
        if (nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new PhotographerException(PhotographerErrorCode.NICKNAME_LENGTH_TOO_LONG);
        }
    }

    private static void validateGenderExists(Gender gender) {
        if (gender == null) {
            throw new PhotographerException(PhotographerErrorCode.GENDER_REQUIRED);
        }
    }

    private static void validateSpecialtyExists(SnapCategory specialty) {
        if (specialty == null) {
            throw new PhotographerException(PhotographerErrorCode.SPECIALTY_REQUIRED);
        }
    }

    private static void validateBioLength(String bio) {
        if (bio != null && bio.length() > MAX_BIO_LENGTH) {
            throw new PhotographerException(PhotographerErrorCode.BIO_TOO_LONG);
        }
    }
}
