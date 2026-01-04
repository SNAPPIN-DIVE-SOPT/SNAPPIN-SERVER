package org.sopt.snappinserver.domain.place.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum PlaceErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    NAME_REQUIRED(400, "PLACE_400_001", "장소 이름은 필수입니다."),
    NAME_TOO_LONG(400, "PLACE_400_002", "장소 이름 길이는 50자 이하입니다."),
    ADDRESS_REQUIRED(400, "PLACE_400_003", "주소는 필수입니다."),
    ADDRESS_TOO_LONG(400, "PLACE_400_004", "주소 길이는 255자 이하입니다."),
    SIDO_REQUIRED(400, "PLACE_400_005", "시/도는 필수입니다."),
    SIDO_TOO_LONG(400, "PLACE_400_006", "시/도 길이는 10자 이하입니다."),
    SIGUNGU_TOO_LONG(400, "PLACE_400_007",  "시/군/구 길이는 50자 이하입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;

}
