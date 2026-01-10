package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimeResult;

@Schema(description = "상품 예약 가능 단일 시간대 응답 DTO")
public record ProductAvailableTimeResponse(
    @Schema(description = "타임슬롯 시작 시간", example = "\"09:00\"")
    String time,

    @Schema(description = "예약 가능 여부", example = "true")
    boolean isAvailable
) {

    public static ProductAvailableTimeResponse from(
        ProductAvailableTimeResult result
    ) {
        return new ProductAvailableTimeResponse(
            result.time().toString(),
            result.isAvailable()
        );
    }
}
