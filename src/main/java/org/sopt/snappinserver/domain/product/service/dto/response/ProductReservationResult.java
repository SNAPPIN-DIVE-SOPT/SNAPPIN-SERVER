package org.sopt.snappinserver.domain.product.service.dto.response;

import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;

public record ProductReservationResult(Long reservationId, ReservationStatus status) {

}
