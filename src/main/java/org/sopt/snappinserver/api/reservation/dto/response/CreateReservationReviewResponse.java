package org.sopt.snappinserver.api.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CreateReservationReviewResult;

@Schema(description = "예약 리뷰 등록 응답 DTO")
public record CreateReservationReviewResponse(

    @Schema(description = "생성된 리뷰 ID", example = "301")
    Long reviewId,

    @Schema(description = "리뷰가 등록된 예약 ID", example = "501")
    Long reservationId
) {

    public static CreateReservationReviewResponse from(
        CreateReservationReviewResult result
    ) {
        return new CreateReservationReviewResponse(
            result.reviewId(),
            result.reservationId()
        );
    }
}
