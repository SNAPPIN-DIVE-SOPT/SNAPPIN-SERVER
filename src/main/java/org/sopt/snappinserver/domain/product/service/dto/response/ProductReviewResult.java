package org.sopt.snappinserver.domain.product.service.dto.response;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.sopt.snappinserver.domain.review.domain.entity.ReviewPhoto;

public record ProductReviewResult(
    Long id,
    String reviewer,
    int rating,
    LocalDate createdAt,
    List<String> images,
    String content
) {

    public static ProductReviewResult from(
        Review review,
        List<ReviewPhoto> reviewPhotos
    ) {
        return new ProductReviewResult(
            review.getId(),
            extractReviewer(review),
            review.getRating(),
            extractCreatedDate(review),
            extractImages(reviewPhotos),
            review.getContent()
        );
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

    private static List<String> extractImages(List<ReviewPhoto> reviewPhotos) {
        return reviewPhotos.stream()
            .sorted(Comparator.comparingInt(ReviewPhoto::getDisplayOrder))
            .map(reviewPhoto -> reviewPhoto.getPhoto().getImageUrl())
            .toList();
    }
}
