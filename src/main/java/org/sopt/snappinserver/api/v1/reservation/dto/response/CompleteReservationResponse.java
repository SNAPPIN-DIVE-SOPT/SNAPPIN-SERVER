package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.service.dto.response.CompleteReservationResult;

public record CompleteReservationResponse(Long reservationId, String status) {

    public static CompleteReservationResponse from(CompleteReservationResult result) {
        return new CompleteReservationResponse(result.reservationId(), result.status().name());
    }
}
