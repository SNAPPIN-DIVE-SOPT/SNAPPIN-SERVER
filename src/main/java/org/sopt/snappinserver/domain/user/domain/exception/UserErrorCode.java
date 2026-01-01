package org.sopt.snappinserver.domain.user.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    USER_ROLE_REQUIRED(400, "USER_400_001", "유저 역할은 필수입니다."),
    GENDER_REQUIRED(400, "USER_400_002", "성별은 필수입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;
}
