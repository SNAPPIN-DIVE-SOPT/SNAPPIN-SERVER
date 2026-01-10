package org.sopt.snappinserver.api.photographer.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum PhotographerSuccessCode implements SuccessCode {

    // 200 OK
    GET_PHOTOGRAPHER_PROFILE_OK(200, "PHOTOGRAPHER_200_001", "성공적으로 작가 프로필을 조회했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
