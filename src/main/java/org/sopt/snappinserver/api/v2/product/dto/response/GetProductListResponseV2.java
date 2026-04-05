package org.sopt.snappinserver.api.v2.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResultV2;

@Schema(description = "상품 목록 조회 응답 DTO")
public record GetProductListResponseV2(

    @Schema(description = "상품 카드 목록")
    List<GetProductCardResponseV2> products
) {

    public static GetProductListResponseV2 from(GetProductListResultV2 result) {
        return new GetProductListResponseV2(
            result.products().stream()
                .map(GetProductCardResponseV2::from)
                .toList()
        );
    }
}
