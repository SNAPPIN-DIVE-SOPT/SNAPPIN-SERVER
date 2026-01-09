package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimesResult;

@Schema(description = "상품 예약 가능 시간대 목록 응답 DTO")
public record ProductAvailableTimesResponse(

    @Schema(description = "조회 기준 날짜", example = "2026-03-15")
    String date,

    @Schema(description = "시간대별 예약 가능 여부 목록")
    List<ProductAvailableTimeResponse> times
) {

    public static ProductAvailableTimesResponse from(
        ProductAvailableTimesResult result
    ) {
        return new ProductAvailableTimesResponse(
            result.date().toString(),
            result.times().stream()
                .map(ProductAvailableTimeResponse::from)
                .toList()
        );
    }
}
