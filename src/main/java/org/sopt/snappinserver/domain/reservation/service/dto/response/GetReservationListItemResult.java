package org.sopt.snappinserver.domain.reservation.service.dto.response;

public record GetReservationListItemResult(
    Long reservationId,
    String status,
    String client,
    String createdAt,
    GetReservationListProductResult product
) {
}
