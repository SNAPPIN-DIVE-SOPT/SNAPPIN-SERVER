package org.sopt.snappinserver.domain.user.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    USER_ROLE_REQUIRED(400, "USER_400_001", "유저 역할은 필수입니다."),
    NAME_REQUIRED(400, "USER_400_002", "이름은 필수입니다."),
    NAME_LENGTH_TOO_LONG(400, "USER_400_003", "이름 길이는 50자 이하입니다."),
    PROFILE_IMAGE_URL_REQUIRED(400, "USER_400_004", "프로필 이미지 url은 필수입니다."),
    PROFILE_IMAGE_URL_TOO_LONG(400, "USER_400_005", "프로필 이미지 url 길이는 1024자 이하입니다."),

    // Onboarding - 이름
    ONBOARDING_NAME_REQUIRED(400, "USER_400_006", "이름은 필수입니다."),
    ONBOARDING_NAME_HAS_WHITESPACE(400, "USER_400_007", "띄어쓰기 없이 작성해주세요."),
    ONBOARDING_NAME_NOT_KOREAN(400, "USER_400_008", "한글로 작성해주세요."),
    ONBOARDING_NAME_TOO_LONG(400, "USER_400_009", "이름을 4글자 이내로 입력해주세요."),

    // Onboarding - 성별
    ONBOARDING_GENDER_REQUIRED(400, "USER_400_010", "성별은 필수입니다."),

    // Onboarding - 닉네임
    ONBOARDING_NICKNAME_REQUIRED(400, "USER_400_011", "닉네임은 필수입니다."),
    ONBOARDING_NICKNAME_HAS_WHITESPACE(400, "USER_400_012", "띄어쓰기 없이 작성해주세요."),
    ONBOARDING_NICKNAME_INVALID(400, "USER_400_013", "한글, 영문, 숫자 2-10자 이내로 입력해주세요."),

    // Onboarding - 전화번호
    ONBOARDING_PHONE_NUMBER_REQUIRED(400, "USER_400_014", "전화번호는 필수입니다."),
    ONBOARDING_PHONE_NUMBER_INVALID(400, "USER_400_015", "정확한 전화번호를 입력해주세요."),

    // Onboarding - 이메일
    ONBOARDING_EMAIL_REQUIRED(400, "USER_400_016", "이메일은 필수입니다."),
    ONBOARDING_EMAIL_INVALID(400, "USER_400_017", "정확한 이메일 주소를 입력해주세요."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN
    SWITCH_PROFILE_FORBIDDEN(403, "USER_403_001", "프로필 전환이 불가능한 사용자입니다."),

    // 404 NOT FOUND
    USER_NOT_FOUND(404, "USER_404_001", "존재하지 않는 사용자입니다."),
    PHOTOGRAPHER_NOT_FOUND(404, "USER_404_002", "해당 사용자의 작가 프로필이 존재하지 않습니다."),
    ONBOARDING_NOT_FOUND(404, "USER_404_003", "해당 사용자의 온보딩 정보가 존재하지 않습니다."),

    // 409 Conflict
    ONBOARDING_ALREADY_SAVED(409, "USER_409_001", "이미 해당 사용자의 온보딩 정보가 존재합니다.");

    ;

    private final int status;
    private final String code;
    private final String message;
}
