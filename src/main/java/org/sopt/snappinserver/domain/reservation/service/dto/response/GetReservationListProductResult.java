package org.sopt.snappinserver.domain.reservation.service.dto.response;

import java.util.List;

public record GetReservationListProductResult(
    Long id,
    String imageUrl,
    String title,
    Double rate,
    int reviewCount,
    String photographer,
    int price,
    List<String> moods,
    boolean isReviewed
) {
}
