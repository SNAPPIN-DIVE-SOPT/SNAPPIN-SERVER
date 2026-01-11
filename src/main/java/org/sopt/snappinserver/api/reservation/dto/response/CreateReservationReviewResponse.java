package org.sopt.snappinserver.api.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "예약 리뷰 등록 응답 DTO")
public record CreateReservationReviewResponse(

    @Schema(description = "생성된 리뷰 ID", example = "301")
    Long reviewId,

    @Schema(description = "리뷰가 등록된 예약 ID", example = "501")
    Long reservationId
) {

    public static CreateReservationReviewResponse of(
        Long reviewId,
        Long reservationId
    ) {
        return new CreateReservationReviewResponse(reviewId, reservationId);
    }
}
