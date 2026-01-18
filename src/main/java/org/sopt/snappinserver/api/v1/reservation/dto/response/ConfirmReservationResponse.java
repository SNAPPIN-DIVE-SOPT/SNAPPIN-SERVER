package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.service.dto.response.ConfirmReservationResult;

public record ConfirmReservationResponse(Long reservationId, String status) {

    public static ConfirmReservationResponse from(ConfirmReservationResult result) {
        return new ConfirmReservationResponse(result.reservationId(), result.status().name());
    }
}
