package org.sopt.snappinserver.domain.review.repository;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductReviewRepositoryImpl implements ProductReviewRepository {

    private final ReviewRepository reviewRepository;

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }
}
