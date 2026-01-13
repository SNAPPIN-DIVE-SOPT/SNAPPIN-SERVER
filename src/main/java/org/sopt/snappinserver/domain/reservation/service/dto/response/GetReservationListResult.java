package org.sopt.snappinserver.domain.reservation.service.dto.response;

import java.util.List;

public record GetReservationListResult(
    List<GetReservationListItemResult> reservations
) {
}
