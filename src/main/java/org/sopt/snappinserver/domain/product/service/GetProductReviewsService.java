package org.sopt.snappinserver.domain.product.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewPageResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductReviewsUseCase;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.sopt.snappinserver.domain.review.domain.entity.ReviewPhoto;
import org.sopt.snappinserver.domain.review.repository.ReviewPhotoRepository;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductReviewsService implements GetProductReviewsUseCase {

    private static final int PAGE_SIZE = 5;
    private static final long MIN_CURSOR_VALUE = 1L;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Override
    public ProductReviewPageResult getProductReviews(Long productId, Long cursor) {

        validateProductExist(productId);
        validateCursorSize(cursor);

        // 리뷰 조회 (Review + Reservation + User fetch join)
        List<Review> reviews =
            (cursor == null)
                ? reviewRepository.findTop6WithUserByProductId(productId)
                : reviewRepository.findTop6WithUserByProductIdAndCursor(productId, cursor);

        boolean hasNext = reviews.size() > PAGE_SIZE;
        if (hasNext) {
            reviews = reviews.subList(0, PAGE_SIZE);
        }

        // 리뷰가 없으면 바로 반환
        if (reviews.isEmpty()) {
            return new ProductReviewPageResult(List.of(), null, false);
        }

        // 리뷰 ID 추출
        List<Long> reviewIds = reviews.stream()
            .map(Review::getId)
            .toList();

        // 리뷰 사진 + 사진 정보 한 번에 조회
        List<ReviewPhoto> reviewPhotos =
            reviewPhotoRepository.findAllByReviewIds(reviewIds);

        // reviewId 기준으로 그룹핑
        Map<Long, List<ReviewPhoto>> photosByReviewId =
            reviewPhotos.stream()
                .collect(Collectors.groupingBy(
                    rp -> rp.getReview().getId()
                ));

        // DTO 변환 (조합 책임은 Service)
        List<ProductReviewResult> results =
            reviews.stream()
                .map(review ->
                    ProductReviewResult.from(
                        review,
                        photosByReviewId.getOrDefault(review.getId(), List.of())
                    )
                )
                .toList();

        Long nextCursor = hasNext
            ? reviews.get(reviews.size() - 1).getId()
            : null;

        return new ProductReviewPageResult(results, nextCursor, hasNext);
    }

    private void validateProductExist(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    private static void validateCursorSize(Long cursor) {
        if (cursor != null && cursor < MIN_CURSOR_VALUE) {
            throw new ProductException(ProductErrorCode.INVALID_CURSOR);
        }
    }
}
