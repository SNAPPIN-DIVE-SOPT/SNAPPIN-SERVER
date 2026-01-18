package org.sopt.snappinserver.api.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Schema(description = "예약 결제 요청 금액 목록 DTO")
public record RequestPaymentReservationRequest(

    @Schema(description = "기본 촬영 비용", example = "80000")
    @NotNull @Positive Integer basePrice,

    @Schema(description = "추가 비용 목록", example = "10000")
    @Valid List<ExtraPriceResult> extraPrices,

    @Schema(description = "최종 결제 금액", example = "90000")
    @NotNull @Positive Integer totalPrice
) {
}
