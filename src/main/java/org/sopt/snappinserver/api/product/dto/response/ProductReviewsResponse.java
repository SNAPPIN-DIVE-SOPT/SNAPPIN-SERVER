package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewPageResult;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "상품 리뷰 커서 기반 목록 응답 DTO")
public class ProductReviewsResponse {

    @Schema(description = "상품 리뷰 목록")
    private List<ProductReviewResponse> reviews;

    public static ProductReviewsResponse from(ProductReviewPageResult result) {
        return new ProductReviewsResponse(
            result.reviews().stream()
                .map(ProductReviewResponse::from)
                .toList()
        );
    }
}
