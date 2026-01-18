package org.sopt.snappinserver.api.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CompleteReservationResult;

@Schema(description = "예약 촬영 완료/리뷰 요청 응답 DTO")
public record CompleteReservationResponse(

    @Schema(description = "촬영 완료 처리된 예약 ID", example = "501")
    Long reservationId,

    @Schema(description = "변경된 예약 상태 ", example = "SHOOT_COMPLETED")
    String status
) {

    public static CompleteReservationResponse from(CompleteReservationResult result) {
        return new CompleteReservationResponse(result.reservationId(), result.status().name());
    }
}
