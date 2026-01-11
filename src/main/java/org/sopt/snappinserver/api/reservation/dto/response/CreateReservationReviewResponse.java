package org.sopt.snappinserver.api.reservation.dto.response;

public record CreateReservationReviewResponse(
    Long reviewId,
    Long reservationId
) {
    public static CreateReservationReviewResponse of(
        Long reviewId,
        Long reservationId
    ) {
        return new CreateReservationReviewResponse(reviewId, reservationId);
    }
}
