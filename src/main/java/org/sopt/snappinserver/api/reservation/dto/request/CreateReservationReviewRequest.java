package org.sopt.snappinserver.api.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "예약 리뷰 등록 요청 DTO")
public record CreateReservationReviewRequest(

    @Schema(description = "리뷰 별점", example = "5", minimum = "1", maximum = "5")
    @NotNull @Min(1) @Max(5)
    Integer rating,

    @Schema(description = "리뷰 내용", example = "촬영이 너무 만족스러웠어요!")
    @NotBlank
    String content,

    @Schema(
        description = "리뷰 이미지 URL 목록",
        example = "[\"https://cdn.example.com/review/1.jpg\", \"https://cdn.example.com/review/2.jpg\"]",
        nullable = true
    )
    List<@NotBlank String> imageUrls
) {
}
