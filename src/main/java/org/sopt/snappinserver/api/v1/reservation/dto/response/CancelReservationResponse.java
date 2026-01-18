package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CancelReservationResult;

@Schema(description = "예약 취소 응답 DTO")
public record CancelReservationResponse(

    @Schema(description = "취소 처리된 예약 ID", example = "501")
    Long reservationId,

    @Schema(description = "취소 처리 이전의 예약 상태", example = "PAYMENT_REQUESTED")
    String previousStatus,

    @Schema(description = "변경된 예약 상태 ", example = "RESERVATION_CANCELED")
    String status
) {

    public static CancelReservationResponse from(CancelReservationResult result) {
        return new CancelReservationResponse(
            result.reservationId(),
            result.previousStatus().name(),
            result.status().name()
        );
    }
}
