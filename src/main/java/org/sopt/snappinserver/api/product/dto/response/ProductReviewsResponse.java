package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.snappinserver.domain.review.service.dto.response.ReviewPageResult;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "상품 리뷰 커서 기반 목록 응답 DTO")
public class ProductReviewsResponse {

    @Schema(description = "상품 리뷰 목록")
    private List<ProductReviewResponse> reviews;

    public static ProductReviewsResponse from(ReviewPageResult result) {
        return new ProductReviewsResponse(
            result.getReviews().stream()
                .map(ProductReviewResponse::from)
                .toList()
        );
    }
}
