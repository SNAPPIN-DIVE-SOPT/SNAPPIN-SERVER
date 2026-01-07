package org.sopt.snappinserver.domain.review.repository;

import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 첫 페이지
    List<Review> findTop6ByReservationProductIdOrderByIdDesc(
            Long productId
    );

    // 커서 이후 페이지
    List<Review> findTop6ByReservationProductIdAndIdLessThanOrderByIdDesc(
            Long productId,
            Long cursor
    );
}
