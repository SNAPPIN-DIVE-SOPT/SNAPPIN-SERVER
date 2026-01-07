package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "상품 리뷰 응답 DTO")
public class ProductReviewResponse {

    @Schema(description = "리뷰 ID", example = "1")
    private Long id;

    @Schema(description = "리뷰 작성자", example = "작성자")
    private String reviewer;

    @Schema(description = "평점", example = "5")
    private int rating;

    @Schema(description = "작성 일자", example = "2026-03-01")
    private LocalDate createdAt;

    @Schema(description = "리뷰 이미지 URL 목록")
    private List<String> images;

    @Schema(description = "리뷰 내용", example = "리뷰 내용")
    private String content;

    public static ProductReviewResponse from(Review review) {
        return new ProductReviewResponse(
                review.getId(),
                getReviewerFrom(review),
                review.getRating(),
                getCreatedDateFrom(review),
                getImagesFrom(review),
                review.getContent()
        );
    }

    private static String getReviewerFrom(Review review) {
        return review.getReservation()
                .getUser()
                .getName();
    }

    private static LocalDate getCreatedDateFrom(Review review) {
        return review.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private static List<String> getImagesFrom(Review review) {
        return review.getReviewPhotos().stream()
                .sorted(Comparator.comparingInt(ReviewPhoto::getDisplayOrder))
                .map(rp -> rp.getPhoto().getImageUrl())
                .toList();
    }
}
