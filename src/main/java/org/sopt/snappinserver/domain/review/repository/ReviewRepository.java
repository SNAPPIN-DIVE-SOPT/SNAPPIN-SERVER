package org.sopt.snappinserver.domain.review.repository;

import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
