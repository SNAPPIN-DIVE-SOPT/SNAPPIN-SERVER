package org.sopt.snappinserver.api.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.RefuseReservationResult;

@Schema(description = "예약 거절 응답 DTO")
public record RefuseReservationResponse(

    @Schema(description = "거절 완료된 예약 ID", example = "51")
    Long reservationId,

    @Schema(description = "변경된 예약 상태", example = "RESERVATION_REFUSED")
    String status
) {

    public static RefuseReservationResponse from(RefuseReservationResult result) {
        return new RefuseReservationResponse(result.reservationId(), result.status().name());
    }
}
