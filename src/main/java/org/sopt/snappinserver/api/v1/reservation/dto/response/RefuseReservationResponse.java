package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.service.dto.response.RefuseReservationResult;

public record RefuseReservationResponse(Long reservationId, String status) {

    public static RefuseReservationResponse from(RefuseReservationResult result) {
        return new RefuseReservationResponse(result.reservationId(), result.status().name());
    }
}
