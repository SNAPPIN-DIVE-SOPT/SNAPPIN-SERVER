package org.sopt.snappinserver.domain.reservation.service.dto.response;

public record CreateReservationReviewResult(
    Long reviewId,
    Long reservationId
) {
    public static CreateReservationReviewResult of(
        Long reviewId,
        Long reservationId
    ) {
        return new CreateReservationReviewResult(reviewId, reservationId);
    }
}
