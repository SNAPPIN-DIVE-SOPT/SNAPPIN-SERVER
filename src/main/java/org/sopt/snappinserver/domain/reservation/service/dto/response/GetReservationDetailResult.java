package org.sopt.snappinserver.domain.reservation.service.dto.response;

import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;

public record GetReservationDetailResult(
    ReservationStatus status,
    GetReservationDetailProductResult product,
    GetReservationDetailInfoResult reservationInfo,
    GetReservationDetailPaymentResult payment,
    GetReservationDetailReviewResult review
) {

}
