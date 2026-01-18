package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailReviewResult;

@Schema(description = "예약 리뷰 상세 응답 DTO")
public record ReservationDetailReviewResponse(

    @Schema(description = "리뷰 ID", example = "40")
    Long id,

    @Schema(description = "작성자명", example = "홍길동")
    String reviewer,

    @Schema(description = "별점", example = "4")
    int rating,

    @Schema(description = "작성일", example = "2026-01-08")
    String createdAt,

    @Schema(description = "이미지 url 목록")
    List<String> images,

    @Schema(description = "리뷰 내용", example = "친절하셔서 좋았어요.")
    String content
) {
    public static ReservationDetailReviewResponse from(
        GetReservationDetailReviewResult result
    ) {
        if (result == null) return null;

        return new ReservationDetailReviewResponse(
            result.id(),
            result.reviewer(),
            result.rating(),
            result.createdAt().toString(),
            result.imageUrls(),
            result.content()
        );
    }
}
