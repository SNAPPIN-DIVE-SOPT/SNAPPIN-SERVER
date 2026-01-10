package org.sopt.snappinserver.api.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishProductResult;

@Schema(description = "상품 좋아요/취소 응답 DTO")
public record WishProductResponse(
    @Schema(description = "상품 아이디", example = "101")
    Long productId,

    @Schema(description = "좋아요 상태 (요청 처리 후)", example = "true")
    boolean liked
) {

    public static WishProductResponse from(WishProductResult result) {
        return new WishProductResponse(result.productId(), Boolean.TRUE.equals(result.liked()));
    }
}
