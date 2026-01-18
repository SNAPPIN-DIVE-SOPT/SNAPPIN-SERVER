package org.sopt.snappinserver.api.v1.reservation.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListResult;

public record ReservationListResponse(
    List<ReservationListItemResponse> reservations
) {

    public static ReservationListResponse from(GetReservationListResult result) {
        return new ReservationListResponse(result
            .reservations()
            .stream()
            .map(ReservationListItemResponse::from)
            .toList()
        );
    }
}
