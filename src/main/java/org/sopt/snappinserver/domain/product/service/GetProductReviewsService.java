package org.sopt.snappinserver.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsCursorResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsCursorResponse;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductReviewsService implements GetProductReviewsUseCase {
    private static final int PAGE_SIZE = 5;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ProductReviewsCursorResponse getProductReviews(
            Long productId,
            Long cursor
    ) {
        // 상품 존재 검증
        if (!productRepository.existsById(productId)) {
            throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        // cursor 검증
        if (cursor != null && cursor < 0) {
            throw new ProductException(ProductErrorCode.INVALID_CURSOR);
        }

        // 리뷰 조회 (limit + 1)
        List<Review> reviews =
                (cursor == null)
                        ? reviewRepository.findTop6ByReservationProductIdOrderByIdDesc(productId)
                        : reviewRepository.findTop6ByReservationProductIdAndIdLessThanOrderByIdDesc(
                        productId, cursor
                );

        // hasNext 판단
        boolean hasNext = reviews.size() > PAGE_SIZE;

        if (hasNext) {
            reviews = reviews.subList(0, PAGE_SIZE);
        }

        // DTO 변환
        List<ProductReviewResponse> reviewResponses =
                reviews.stream()
                        .map(ProductReviewResponse::from)
                        .toList();

        // nextCursor 계산
        Long nextCursor = hasNext
                ? reviews.get(reviews.size() - 1).getId()
                : null;

        return new ProductReviewsCursorResponse(
                reviewResponses,
                nextCursor,
                hasNext
        );
    }
}
