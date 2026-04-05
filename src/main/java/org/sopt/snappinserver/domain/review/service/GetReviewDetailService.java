package org.sopt.snappinserver.domain.review.service;

import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewErrorCode;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewException;
import org.sopt.snappinserver.domain.review.repository.ReviewPhotoRepository;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.sopt.snappinserver.domain.review.service.dto.response.GetReviewDetailResult;
import org.sopt.snappinserver.domain.review.service.usecase.GetReviewDetailUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GetReviewDetailService implements GetReviewDetailUseCase {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetReviewDetailResult getReviewDetail(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        List<String> images = reviewPhotoRepository
            .findAllByReviewIds(List.of(reviewId))
            .stream()
            .map(rp -> cloudFrontDomain + rp.getPhoto().getImageUrl())
            .toList();

        return new GetReviewDetailResult(
            review.getId(),
            review.resolveReviewerName(),
            review.getRating(),
            review.getCreatedAt().atZone(KOREA_ZONE).toLocalDate(),
            images,
            review.getContent()
        );
    }
}
