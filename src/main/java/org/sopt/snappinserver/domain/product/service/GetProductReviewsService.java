package org.sopt.snappinserver.domain.product.service;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductReviewsUseCase;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.sopt.snappinserver.domain.review.domain.entity.ReviewPhoto;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.sopt.snappinserver.domain.review.service.dto.response.ReviewPageResult;
import org.sopt.snappinserver.domain.review.service.dto.response.ReviewResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductReviewsService implements GetProductReviewsUseCase {

    private static final int PAGE_SIZE = 5;
    private static final long MIN_CURSOR_VALUE = 1L;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewPageResult getProductReviews(Long productId, Long cursor) {

        // 상품 및 커서 유효성 검증
        if (!productRepository.existsById(productId)) {
            throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        if (cursor != null && cursor < MIN_CURSOR_VALUE) {
            throw new ProductException(ProductErrorCode.INVALID_CURSOR);
        }

        // 커서 기준으로 리뷰 조회 (limit + 1)
        List<Review> reviews =
            (cursor == null)
                ? reviewRepository.findTop6ByReservationProductIdOrderByIdDesc(productId)
                : reviewRepository
                    .findTop6ByReservationProductIdAndIdLessThanOrderByIdDesc(productId, cursor);

        // 다음 페이지 존재 여부 판단
        boolean hasNext = reviews.size() > PAGE_SIZE;

        if (hasNext) {
            reviews = reviews.subList(0, PAGE_SIZE);
        }

        // 엔티티를 도메인 결과 DTO로 변환
        List<ReviewResult> results =
            reviews.stream()
                .map(this::toResult)
                .toList();

        // 다음 커서 계산
        Long nextCursor = hasNext
            ? reviews.get(reviews.size() - 1).getId()
            : null;

        return new ReviewPageResult(results, nextCursor, hasNext);
    }

    private ReviewResult toResult(Review review) {
        return new ReviewResult(
            review.getId(),
            review.getReservation().getUser().getName(),
            review.getRating(),
            review.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toLocalDate(),
            review.getReviewPhotos().stream()
                .sorted(Comparator.comparingInt(ReviewPhoto::getDisplayOrder))
                .map(rp -> rp.getPhoto().getImageUrl())
                .toList(),
            review.getContent()
        );
    }
}
