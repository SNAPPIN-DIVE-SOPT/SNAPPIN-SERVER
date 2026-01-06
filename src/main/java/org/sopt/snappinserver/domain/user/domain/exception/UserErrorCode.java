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

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;
}
