package org.sopt.snappinserver.api.auth.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum AuthSuccessCode implements SuccessCode {

    // 200 OK
    LOGIN_SUCCESS(200, "AUTH_200_001", "성공적으로 카카오 로그인을 완료했습니다."),
    REISSUE_TOKENS_SUCCESS(200, "AUTH_200_002", "성공적으로 토큰 재발급을 완료했습니다."),
    LOGOUT_SUCCESS(200, "AUTH_200_003", "성공적으로 로그아웃을 완료했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
