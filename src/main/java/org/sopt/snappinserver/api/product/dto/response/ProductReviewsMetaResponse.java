package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.snappinserver.domain.review.service.dto.response.ReviewPageResult;

@Schema(description = "상품 리뷰 커서 기반 조회 메타 정보")
@Getter
@AllArgsConstructor
public class ProductReviewsMetaResponse {

    @Schema(description = "다음 커서 값", example = "6", nullable = true)
    private Long nextCursor;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    public static ProductReviewsMetaResponse from(ReviewPageResult result) {
        return new ProductReviewsMetaResponse(
            result.getNextCursor(),
            result.isHasNext()
        );
    }
}
