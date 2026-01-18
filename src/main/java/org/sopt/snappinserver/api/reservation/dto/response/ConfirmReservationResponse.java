package org.sopt.snappinserver.api.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.ConfirmReservationResult;

@Schema(description = "예약 확정 응답 DTO")
public record ConfirmReservationResponse(

    @Schema(description = "확정된 예약 ID", example = "501")
    Long reservationId,

    @Schema(description = "변경된 예약 상태", example = "RESERVATION_CONFIRMED")
    String status
) {

    public static ConfirmReservationResponse from(ConfirmReservationResult result) {
        return new ConfirmReservationResponse(result.reservationId(), result.status().name());
    }
}
