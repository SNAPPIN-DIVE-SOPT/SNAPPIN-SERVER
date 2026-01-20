package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductDurationTimeResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;

@Schema(description = "상품 촬영 시간 조회 응답 DTO")
public record ProductDurationTimeResponse(

    @Schema(description = "촬영 최소 시간", example = "1")
    double minDurationTime
) {

    public static ProductDurationTimeResponse from(ProductDurationTimeResult result) {
        return new ProductDurationTimeResponse(result.minDurationTime());
    }
}
