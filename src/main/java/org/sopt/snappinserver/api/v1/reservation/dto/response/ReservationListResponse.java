package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListResult;

@Schema(description = "예약 목록 조회 응답 DTO")
public record ReservationListResponse(

    @Schema(description = "예약 목록")
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
