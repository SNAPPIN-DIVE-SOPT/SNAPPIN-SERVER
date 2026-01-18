package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPriceResult;

@Schema(description = "상품 기본 촬영 비용 응답 DTO")
public record ProductPriceResponse(

    @Schema(description = "상품 가격", example = "80000")
    int price
) {

    public static ProductPriceResponse from(ProductPriceResult result) {
        return new ProductPriceResponse(result.price());
    }
}
