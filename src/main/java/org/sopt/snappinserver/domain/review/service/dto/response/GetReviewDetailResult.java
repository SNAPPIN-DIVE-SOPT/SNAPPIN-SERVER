package org.sopt.snappinserver.domain.review.service.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GetReviewDetailResult(
    Long id,
    String reviewer,
    Integer rating,
    LocalDate createdAt,
    List<String> images,
    String content
) {
}
