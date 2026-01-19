package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "결제 정보 DTO")
public record PaymentResponse(

    @Schema(description = "기본 촬영 비용", example = "200000")
    int basePrice,

    @Schema(description = "추가 비용 목록")
    List<ExtraPriceResponse> extraPrices,

    @Schema(description = "최종 결제 금액", example = "210000")
    int totalPrice
) {

}
