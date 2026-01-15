package org.sopt.snappinserver.api.reservation.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ReservationSuccessCode implements SuccessCode {

    // 200 OK
    GET_RESERVATION_LIST_OK(
        200,
        "RESERVATION_200_001",
        "예약 목록 조회에 성공했습니다."
    ),
    GET_RESERVATION_DETAIL_OK(
        200,
        "RESERVATION_200_002",
        "예약 상세 및 촬영 내역 조회에 성공했습니다."
    ),
    PATCH_RESERVATION_PAY_OK(
        200,
        "RESERVATION_200_003",
        "예약 결제에 성공했습니다."
    ),
    PATCH_RESERVATION_CANCEL_OK(
        200,
        "RESERVATION_200_004",
        "예약 취소에 성공했습니다."
    ),
    PATCH_RESERVATION_COMPLETE_OK(
        200,
        "RESERVATION_200_005",
        "촬영 완료 처리에 성공했습니다."
    ),
    PATCH_RESERVATION_CONFIRM_OK(
        200,
        "RESERVATION_200_006",
        "예약 확정 처리에 성공했습니다."
    ),
    PATCH_RESERVATION_REFUSE_OK(
        200,
        "RESERVATION_200_007",
        "예약 거절 처리에 성공했습니다."
    ),
    PATCH_RESERVATION_REQUEST_PAYMENT_OK(
        200,
        "RESERVATION_200_008",
        "결제 요청 처리에 성공했습니다."
    ),

    // 201 CREATED
    POST_RESERVATION_REVIEW_CREATED(201, "RESERVATION_201_001", "예약 리뷰 등록에 성공했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
