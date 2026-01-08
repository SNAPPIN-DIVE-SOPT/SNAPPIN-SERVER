package org.sopt.snappinserver.domain.review.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    // 첫 페이지 조회 (cursor 없음)
    @Query("""
        select review
        from Review review
        join fetch review.reservation reservation
        join fetch reservation.user user
        where reservation.product.id = :productId
        order by review.id desc
    """)
    List<Review> findReviewsWithUserByProductId(
        @Param("productId") Long productId,
        Pageable pageable
    );

    // 커서 이후 페이지 조회
    @Query("""
        select review
        from Review review
        join fetch review.reservation reservation
        join fetch reservation.user user
        where reservation.product.id = :productId
          and review.id < :cursor
        order by review.id desc
    """)
    List<Review> findReviewsWithUserByProductIdAndCursor(
        @Param("productId") Long productId,
        @Param("cursor") Long cursor,
        Pageable pageable
    );
}
