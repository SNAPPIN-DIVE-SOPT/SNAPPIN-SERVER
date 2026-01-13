package org.sopt.snappinserver.api.reservation.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListProductResult;

public record ReservationListProductResponse(
    Long id,
    String imageUrl,
    String title,
    double rate,
    int reviewCount,
    String photographer,
    int price,
    List<String> moods,
    boolean isReviewed
) {

    public static ReservationListProductResponse from(
        GetReservationListProductResult result
    ) {
        return new ReservationListProductResponse(
            result.id(),
            result.imageUrl(),
            result.title(),
            result.rate(),
            result.reviewCount(),
            result.photographer(),
            result.price(),
            result.moods(),
            result.isReviewed()
        );
    }
}
