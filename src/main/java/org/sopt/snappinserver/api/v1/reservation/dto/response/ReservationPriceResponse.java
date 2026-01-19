package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.ReservationPriceResult;

@Schema(description = "예약 상품 기본 촬영 비용 응답 DTO")
public record ReservationPriceResponse(

    @Schema(description = "예약 ID", example = "501")
    Long reservationId,

    @Schema(description = "예약 상품 가격", example = "80000")
    int price
) {

    public static ReservationPriceResponse from(ReservationPriceResult result) {
        return new ReservationPriceResponse(result.reservationId(), result.price());
    }
}
