package org.sopt.snappinserver.api.v1.review.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.sopt.snappinserver.domain.review.service.dto.response.GetReviewDetailResult;

public record GetReviewDetailResponse(
    Long id,
    String reviewer,
    Integer rating,
    LocalDate createdAt,
    List<String> images,
    String content
) {

    public static GetReviewDetailResponse from(GetReviewDetailResult result) {
        return new GetReviewDetailResponse(
            result.id(),
            result.reviewer(),
            result.rating(),
            result.createdAt(),
            result.images(),
            result.content()
        );
    }
}
