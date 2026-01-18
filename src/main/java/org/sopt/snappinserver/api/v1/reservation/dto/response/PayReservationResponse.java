package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.PayReservationResult;

@Schema(description = "예약 결제하기 응답 DTO")
public record PayReservationResponse(

    @Schema(description = "결제 완료된 예약 아이디", example = "501")
    Long reservationId,

    @Schema(description = "변경된 예약 상태 ", example = "PAYMENT_COMPLETED")
    String status
) {

    public static PayReservationResponse from(PayReservationResult result) {
        return new PayReservationResponse(result.reservationId(), result.status().name());
    }
}
