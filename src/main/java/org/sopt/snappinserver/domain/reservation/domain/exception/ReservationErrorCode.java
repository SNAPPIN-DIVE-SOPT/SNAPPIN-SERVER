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
    RESERVATION_REQUIRED(400, "RESERVATION_400_009", "예약은 필수입니다."),
    ADDITIONAL_PAYMENT_NAME_REQUIRED(400, "RESERVATION_400_010", "추가 요청 금액명은 필수입니다."),
    ADDITIONAL_PAYMENT_NAME_TOO_LONG(400, "RESERVATION_400_011", "추가 요청 금액명 길이는 100자 이하입니다."),
    ADDITIONAL_PAYMENT_AMOUNT_REQUIRED(400, "RESERVATION_400_012", "추가 요청 금액은 필수입니다."),
    ADDITIONAL_PAYMENT_AMOUNT_TOO_SMALL(400, "RESERVATION_400_013", "추가 요청 금액은 10원 이상이어야 합니다."),
    INVALID_BASE_PRICE(400, "RESERVATION_400_014", "기본 촬영 금액이 상품 가격과 일치하지 않습니다."),
    INVALID_TOTAL_PRICE(400, "RESERVATION_400_015", "기본 금액과 추가 금액의 합이 최종 결제 금액과 일치하지 않습니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN
    RESERVATION_USER_NOT_MATCH(403, "RESERVATION_403_001", "사용자가 예약의 소유자가 아닙니다."),

    // 404 NOT FOUND
    RESERVATION_NOT_FOUND(404, "RESERVATION_404_001", "존재하지 않는 예약입니다."),

    // 409 CONFLICT
    RESERVATION_TIME_CONFLICT(409, "RESERVATION_409_001", "해당 시간에 이미 예약이 존재합니다."),
    RESERVATION_NOT_COMPLETED(409, "RESERVATION_409_002", "촬영이 완료된 예약만 리뷰를 작성할 수 있습니다."),
    RESERVATION_NOT_PAYMENT_REQUESTED(409, "RESERVATION_409_003", "결제 요청 상태의 예약만 결제를 진행할 수 있습니다."),
    RESERVATION_ALREADY_COMPLETED(409, "RESERVATION_409_004", "촬영 완료된 예약은 취소할 수 없습니다."),
    RESERVATION_ALREADY_CANCELED(409, "RESERVATION_409_005", "이미 취소된 예약입니다."),
    RESERVATION_NOT_CONFIRMED(409, "RESERVATION_409_006", "예약 확정 상태의 예약만 촬영 완료 처리할 수 있습니다."),
    RESERVATION_NOT_PAYMENT_COMPLETED(409, "RESERVATION_409_007", "결제된 예약만 예약 확정 처리할 수 있습니다."),
    RESERVATION_CANNOT_REFUSE(409, "RESERVATION_409_008", "현재 예약 상태에서는 예약 거절이 불가합니다."),
    RESERVATION_ALREADY_REFUSED(409, "RESERVATION_409_009", "이미 거절된 예약입니다."),
    RESERVATION_CANNOT_REQUEST_PAYMENT(409, "RESERVATION_409_010", "작가 확인 중 상태의 예약에만 결제를 요청할 수 있습니다.");

    private final int status;
    private final String code;
    private final String message;
}
