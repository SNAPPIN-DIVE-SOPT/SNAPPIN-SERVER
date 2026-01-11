package org.sopt.snappinserver.domain.reservation.service.dto.request;

import java.util.List;

public record CreateReservationReviewCommand(
    Long userId,
    Long reservationId,
    Integer rating,
    String content,
    List<String> imageUrls
) {
}

