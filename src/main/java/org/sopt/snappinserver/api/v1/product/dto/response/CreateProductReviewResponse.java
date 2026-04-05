package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.CreateProductReviewResult;

@Schema(description = "상품 리뷰 등록 응답 DTO")
public record CreateProductReviewResponse(

    @Schema(description = "생성된 리뷰 ID", example = "301")
    Long reviewId,

    @Schema(description = "리뷰가 등록된 상품 ID", example = "12")
    Long productId
) {

    public static CreateProductReviewResponse from(CreateProductReviewResult result) {
        return new CreateProductReviewResponse(
            result.reviewId(),
            result.productId()
        );
    }
}
