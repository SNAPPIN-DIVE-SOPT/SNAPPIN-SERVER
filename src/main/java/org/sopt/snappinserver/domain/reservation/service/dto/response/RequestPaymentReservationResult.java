package org.sopt.snappinserver.domain.reservation.service.dto.response;

import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;

public record RequestPaymentReservationResult(
    Long reservationId,
    ReservationStatus status,
    int basePrice,
    int extraPrice,
    int totalPrice
) {

}
