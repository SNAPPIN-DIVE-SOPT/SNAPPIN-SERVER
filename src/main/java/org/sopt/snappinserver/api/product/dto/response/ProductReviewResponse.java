package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewResult;

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

    public static ProductReviewResponse from(ProductReviewResult result) {
        return new ProductReviewResponse(
            result.getId(),
            result.getReviewer(),
            result.getRating(),
            result.getCreatedAt(),
            result.getImages(),
            result.getContent()
        );
    }
}
