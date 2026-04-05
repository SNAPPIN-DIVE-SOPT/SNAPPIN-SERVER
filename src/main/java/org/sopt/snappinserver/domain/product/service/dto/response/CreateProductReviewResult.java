package org.sopt.snappinserver.domain.product.service.dto.response;

public record CreateProductReviewResult(Long reviewId, Long productId) {

    public static CreateProductReviewResult of(Long reviewId, Long productId) {
        return new CreateProductReviewResult(reviewId, productId);
    }
}
