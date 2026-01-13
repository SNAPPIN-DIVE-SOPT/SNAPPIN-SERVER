package org.sopt.snappinserver.api.reservation.dto.response;

import java.util.List;

public record ReservationDetailProductResponse(
    Long id,
    String imageUrl,
    String title,
    double rate,
    int reviewCount,
    String photographer,
    int price,
    List<String> moods
) {
    public static ReservationDetailProductResponse from(
        org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailProductResult result
    ) {
        return new ReservationDetailProductResponse(
            result.id(),
            result.imageUrl(),
            result.title(),
            result.rate(),
            result.reviewCount(),
            result.photographer(),
            result.price(),
            result.moods()
        );
    }
}
