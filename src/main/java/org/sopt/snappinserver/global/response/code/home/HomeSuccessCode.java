package org.sopt.snappinserver.global.response.code.home;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum HomeSuccessCode implements SuccessCode {

    // 200 OK
    GET_PLACE_PHOTOGRAPHER_RECOMMENDATION_OK(200, "HOME_200_001", "성공적으로 장소, 작가 추천 목록을 조회했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
