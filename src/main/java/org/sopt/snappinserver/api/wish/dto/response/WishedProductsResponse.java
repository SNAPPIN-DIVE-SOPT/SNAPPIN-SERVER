package org.sopt.snappinserver.api.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductsResult;

@Schema(description = "위시 상품 목록 조회 응답 DTO")
public record WishedProductsResponse(

    @Schema(description = "좋아요한 상품 목록")
    List<WishedProductResponse> products
) {

    public static WishedProductsResponse from(WishedProductsResult result) {
        return new WishedProductsResponse(
            result.products().stream().map(WishedProductResponse::from).toList()
        );
    }
}
