package org.sopt.snappinserver.domain.reservation.service.dto.response;

import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;

public record CancelReservationResult(
    Long reservationId,
    ReservationStatus previousStatus,
    ReservationStatus status
) {

}
