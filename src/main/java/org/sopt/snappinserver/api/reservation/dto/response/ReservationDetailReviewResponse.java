package org.sopt.snappinserver.api.reservation.dto.response;

import java.util.List;

public record ReservationDetailReviewResponse(
    Long id,
    String reviewer,
    int rating,
    String createdAt,
    List<String> images,
    String content
) {
    public static ReservationDetailReviewResponse from(
        org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailReviewResult result
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
