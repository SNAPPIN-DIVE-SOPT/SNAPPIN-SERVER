package org.sopt.snappinserver.api.v1.place.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum PlaceSuccessCode implements SuccessCode {

    // 200 OK
    GET_PLACE_LIST_OK(200, "PLACE_200_001", "성공적으로 촬영 가능 장소 목록을 조회했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
