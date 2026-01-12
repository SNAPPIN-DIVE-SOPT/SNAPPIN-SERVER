package org.sopt.snappinserver.api.reservation.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ReservationSuccessCode implements SuccessCode {

    // 200 OK

    // 201 CREATED
    POST_RESERVATION_REVIEW_CREATED(201, "RESERVATION_201_001", "예약 리뷰 등록에 성공했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
