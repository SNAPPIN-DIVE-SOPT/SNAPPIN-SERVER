package org.sopt.snappinserver.domain.reservation.domain.enums;

import static org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus.PAYMENT_REQUESTED;
import static org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus.PHOTOGRAPHER_CHECKING;
import static org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus.RESERVATION_CANCELED;
import static org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus.RESERVATION_CONFIRMED;
import static org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus.RESERVATION_REFUSED;
import static org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus.RESERVATION_REQUESTED;
import static org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus.SHOOT_COMPLETED;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatusTab {

    CLIENT_OVERVIEW(
        "예약 현황",
        List.of(RESERVATION_REQUESTED, PHOTOGRAPHER_CHECKING, PAYMENT_REQUESTED, PAYMENT_COMPLETED,
            RESERVATION_CANCELED, RESERVATION_REFUSED)
    ),

    CLIENT_DONE("촬영 완료", List.of(SHOOT_COMPLETED)),

    PHOTOGRAPHER_REQUESTED("예약 요청", List.of(RESERVATION_REQUESTED)),

    PHOTOGRAPHER_ADJUSTING(
        "조율 중",
        List.of(PHOTOGRAPHER_CHECKING, PAYMENT_REQUESTED, PAYMENT_COMPLETED)
    ),

    PHOTOGRAPHER_CONFIRMED("예약 확정", List.of(RESERVATION_CONFIRMED)),

    PHOTOGRAPHER_DONE("촬영 완료", List.of(SHOOT_COMPLETED)),
    ;

    private final String tab;
    private final List<ReservationStatus> relatedStatus;

}
