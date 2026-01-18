package org.sopt.snappinserver.api.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailPaymentResult;

@Schema(description = "예약 상세 결제 정보 DTO")
public record ReservationDetailPaymentResponse(

    @Schema(description = "기본 촬영 비용", example = "80000")
    int basePrice,

    @Schema(description = "추가 비용", example = "10000")
    int extraPrice,

    @Schema(description = "최종 결제 금액", example = "90000")
    int totalPrice
) {

    public static ReservationDetailPaymentResponse from(GetReservationDetailPaymentResult result) {
        if (result == null) {
            return null;
        }

        return new ReservationDetailPaymentResponse(
            result.basePrice(),
            result.extraPrice(),
            result.totalPrice()
        );
    }
}
