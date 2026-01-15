package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResult;

@Schema(description = "상품 목록 응답 DTO")
public record GetProductListResponse(

    @Schema(description = "상품 목록")
    List<GetProductCardResponse> products
) {

    public static GetProductListResponse from(GetProductListResult result) {
        return new GetProductListResponse(
            result.products().stream()
                .map(GetProductCardResponse::from)
                .toList()
        );
    }
}
