package org.sopt.snappinserver.api.v1.reservation.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailProductResult;

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

    public static ReservationDetailProductResponse from(GetReservationDetailProductResult result) {
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
