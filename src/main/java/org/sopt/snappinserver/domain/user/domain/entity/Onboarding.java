package org.sopt.snappinserver.domain.user.domain.entity;

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
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.global.entity.BaseEntity;
import org.sopt.snappinserver.global.enums.Gender;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "onboarding")
public class Onboarding extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 4;
    private static final int MAX_NICKNAME_LENGTH = 10;
    private static final int MAX_PHONE_NUMBER_LENGTH = 13;
    private static final int MAX_EMAIL_LENGTH = 320;

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile(".*\\s.*");
    private static final Pattern KOREAN_ONLY_PATTERN = Pattern.compile("^[가-힣]+$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]{2,10}$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{3}-\\d{4}-\\d{4}$");
    private static final Pattern EMAIL_PATTERN = Pattern
        .compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "onboarding_seq_gen")
    @SequenceGenerator(
        name = "onboarding_seq_gen",
        sequenceName = "onboarding_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    @Column(nullable = false, length = MAX_PHONE_NUMBER_LENGTH)
    private String phoneNumber;

    @Column(nullable = false, length = MAX_EMAIL_LENGTH)
    private String email;

    @Builder(access = AccessLevel.PRIVATE)
    private Onboarding(
        User user,
        String name,
        Gender gender,
        String nickname,
        String phoneNumber,
        String email
    ) {
        this.user = user;
        this.name = name;
        this.gender = gender;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public static Onboarding create(
        User user,
        String name,
        Gender gender,
        String nickname,
        String phoneNumber,
        String email
    ) {
        validateOnboarding(name, gender, nickname, phoneNumber, email);
        return Onboarding.builder()
            .user(user)
            .name(name)
            .gender(gender)
            .nickname(nickname)
            .phoneNumber(phoneNumber)
            .email(email)
            .build();
    }

    private static void validateOnboarding(
        String name,
        Gender gender,
        String nickname,
        String phoneNumber,
        String email
    ) {
        validateName(name);
        validateGender(gender);
        validateNickname(nickname);
        validatePhoneNumber(phoneNumber);
        validateEmail(email);
    }

    private static void validateName(String name) {
        validateNameExists(name);
        validateNotIncludeWhitespace(name);
        validateNamePattern(name);
        validateNameLength(name);
    }

    private static void validateNameExists(String name) {
        if (name == null || name.isBlank()) {
            throw new UserException(UserErrorCode.ONBOARDING_NAME_REQUIRED);
        }
    }

    private static void validateNotIncludeWhitespace(String name) {
        if (WHITESPACE_PATTERN.matcher(name).matches()) {
            throw new UserException(UserErrorCode.ONBOARDING_NAME_HAS_WHITESPACE);
        }
    }

    private static void validateNamePattern(String name) {
        if (!KOREAN_ONLY_PATTERN.matcher(name).matches()) {
            throw new UserException(UserErrorCode.ONBOARDING_NAME_NOT_KOREAN);
        }
    }

    private static void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new UserException(UserErrorCode.ONBOARDING_NAME_TOO_LONG);
        }
    }

    private static void validateGender(Gender gender) {
        if (gender == null) {
            throw new UserException(UserErrorCode.ONBOARDING_GENDER_REQUIRED);
        }
    }

    private static void validateNickname(String nickname) {
        validateNicknameExists(nickname);
        validateNickNameNotIncludeWhitespace(nickname);
        validateNickNamePattern(nickname);
    }

    private static void validateNicknameExists(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new UserException(UserErrorCode.ONBOARDING_NICKNAME_REQUIRED);
        }
    }

    private static void validateNickNameNotIncludeWhitespace(String nickname) {
        if (WHITESPACE_PATTERN.matcher(nickname).matches()) {
            throw new UserException(UserErrorCode.ONBOARDING_NICKNAME_HAS_WHITESPACE);
        }
    }

    private static void validateNickNamePattern(String nickname) {
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new UserException(UserErrorCode.ONBOARDING_NICKNAME_INVALID);
        }
    }

    private static void validatePhoneNumber(String phoneNumber) {
        validatePhoneNumberExists(phoneNumber);
        validatePhoneNumberPattern(phoneNumber);
    }

    private static void validatePhoneNumberExists(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new UserException(UserErrorCode.ONBOARDING_PHONE_NUMBER_REQUIRED);
        }
    }

    private static void validatePhoneNumberPattern(String phoneNumber) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new UserException(UserErrorCode.ONBOARDING_PHONE_NUMBER_INVALID);
        }
    }

    private static void validateEmail(String email) {
        validateEmailExists(email);
        validateEmailPattern(email);
    }

    private static void validateEmailExists(String email) {
        if (email == null || email.isBlank()) {
            throw new UserException(UserErrorCode.ONBOARDING_EMAIL_REQUIRED);
        }
    }

    private static void validateEmailPattern(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new UserException(UserErrorCode.ONBOARDING_EMAIL_INVALID);
        }
    }
}
