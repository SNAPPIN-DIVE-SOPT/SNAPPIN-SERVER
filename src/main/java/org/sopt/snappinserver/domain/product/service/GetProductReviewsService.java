package org.sopt.snappinserver.domain.product.service;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
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

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewPageResult getProductReviews(Long productId, Long cursor) {

        if (!productRepository.existsById(productId)) {
            throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        if (cursor != null && cursor < 0) {
            throw new ProductException(ProductErrorCode.INVALID_CURSOR);
        }

        List<Review> reviews =
                (cursor == null)
                        ? reviewRepository.findTop6ByReservationProductIdOrderByIdDesc(productId)
                        : reviewRepository
                        .findTop6ByReservationProductIdAndIdLessThanOrderByIdDesc(productId, cursor);

        boolean hasNext = reviews.size() > PAGE_SIZE;

        if (hasNext) {
            reviews = reviews.subList(0, PAGE_SIZE);
        }

        List<ReviewResult> results =
                reviews.stream()
                        .map(this::toResult)
                        .toList();

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
