package org.sopt.snappinserver.api.user.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements SuccessCode {

    // 200 OK
    GET_USER_INFO_OK(200, "USER_200_001", "성공적으로 유저 정보를 조회했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
