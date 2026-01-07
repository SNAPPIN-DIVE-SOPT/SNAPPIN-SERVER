package org.sopt.snappinserver.domain.product.service.dto.response;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.sopt.snappinserver.domain.review.domain.entity.ReviewPhoto;

@Getter
@AllArgsConstructor
public class ProductReviewResult {

    private Long id;
    private String reviewer;
    private int rating;
    private LocalDate createdAt;
    private List<String> images;
    private String content;

    public static ProductReviewResult from(Review review) {
        return new ProductReviewResult(
            review.getId(),
            extractReviewer(review),
            review.getRating(),
            extractCreatedDate(review),
            extractImages(review),
            review.getContent()
        );
    }

    public static List<ProductReviewResult> of(List<Review> reviews) {
        return reviews.stream()
            .map(ProductReviewResult::from)
            .toList();
    }
    private static String extractReviewer(Review review) {
        return review.getReservation()
            .getUser()
            .getName();
    }

    private static LocalDate extractCreatedDate(Review review) {
        return review.getCreatedAt()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }

    private static List<String> extractImages(Review review) {
        return review.getReviewPhotos().stream()
            .sorted(Comparator.comparingInt(ReviewPhoto::getDisplayOrder))
            .map(rp -> rp.getPhoto().getImageUrl())
            .toList();
    }
}
