package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.service.dto.response.PayReservationResult;

public record PayReservationResponse(Long reservationId, String status) {

    public static PayReservationResponse from(PayReservationResult result) {
        return new PayReservationResponse(result.reservationId(), result.status().name());
    }
}
