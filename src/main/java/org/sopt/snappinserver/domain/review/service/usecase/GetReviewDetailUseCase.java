package org.sopt.snappinserver.domain.review.service.usecase;

import org.sopt.snappinserver.domain.review.service.dto.response.GetReviewDetailResult;

public interface GetReviewDetailUseCase {

    GetReviewDetailResult getReviewDetail(Long userId, Long reviewId);
}
