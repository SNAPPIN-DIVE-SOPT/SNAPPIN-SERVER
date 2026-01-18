package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListItemResult;

public record ReservationListItemResponse(
    Long reservationId,
    String status,
    String client,
    String createdAt,
    ReservationListProductResponse product
) {

    public static ReservationListItemResponse from(GetReservationListItemResult result) {
        return new ReservationListItemResponse(
            result.reservationId(),
            result.status(),
            result.client(),
            result.createdAt(),
            ReservationListProductResponse.from(result.product())
        );
    }
}
