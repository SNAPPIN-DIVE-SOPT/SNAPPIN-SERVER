package org.sopt.snappinserver.domain.reservation.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    PRODUCT_REQUIRED(400, "RESERVATION_400_001", "예약한 상품은 필수입니다."),
    USER_REQUIRED(400, "RESERVATION_400_002", "예약한 고객은 필수입니다."),
    PLACE_REQUIRED(400, "RESERVATION_400_003", "촬영 장소는 필수입니다."),
    RESERVED_AT_REQUIRED(400, "RESERVATION_400_004", "예약 촬영 날짜는 필수입니다."),
    DURATION_TIME_REQUIRED(400, "RESERVATION_400_005", "촬영 소요 시간(분 단위)은 필수입니다."),
    PEOPLE_COUNT_REQUIRED(400, "RESERVATION_400_006", "촬영 인원은 필수입니다."),
    REQUEST_NOTE_TOO_LONG(400, "RESERVATION_400_007", "기타 요청사항 길이는 512자 이하입니다."),
    RESERVATION_STATUS_REQUIRED(400, "RESERVATION_400_008", "예약 상태는 필수입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;
}
