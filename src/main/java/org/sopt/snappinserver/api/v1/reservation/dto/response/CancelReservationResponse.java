package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.service.dto.response.CancelReservationResult;

public record CancelReservationResponse(Long reservationId, String previousStatus, String status) {

    public static CancelReservationResponse from(CancelReservationResult result) {
        return new CancelReservationResponse(
            result.reservationId(),
            result.previousStatus().name(),
            result.status().name()
        );
    }
}
