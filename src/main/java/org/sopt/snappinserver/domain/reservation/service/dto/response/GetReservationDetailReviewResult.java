package org.sopt.snappinserver.domain.reservation.service.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GetReservationDetailReviewResult(
    Long id,
    String reviewer,
    int rating,
    LocalDate createdAt,
    List<String> imageUrls,
    String content
) {

}
