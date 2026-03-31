package org.sopt.snappinserver.global.response.code.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements SuccessCode {

    // 200 OK
    GET_USER_INFO_OK(200, "USER_200_001", "성공적으로 유저 정보를 조회했습니다."),
    SWITCH_USER_ROLE_OK(200, "USER_200_002", "성공적으로 유저 역할을 전환했습니다."),
    CREATE_ONBOARDING_OK(200, "USER_200_003", "성공적으로 온보딩 정보를 저장했습니다."),
    GET_ONBOARDING_OK(200, "USER_200_004", "성공적으로 온보딩 정보를 조회했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
