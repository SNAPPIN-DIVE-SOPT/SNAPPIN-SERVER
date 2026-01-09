package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewPageResult;

@Schema(description = "상품 리뷰 커서 기반 조회 메타 정보 DTO")
public record ProductReviewsMetaResponse(

    @Schema(description = "다음 커서 값", example = "6", nullable = true)
    Long nextCursor,

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    boolean hasNext

) {
    public static ProductReviewsMetaResponse from(ProductReviewPageResult result) {
        return new ProductReviewsMetaResponse(
            result.nextCursor(),
            result.hasNext()
        );
    }
}
