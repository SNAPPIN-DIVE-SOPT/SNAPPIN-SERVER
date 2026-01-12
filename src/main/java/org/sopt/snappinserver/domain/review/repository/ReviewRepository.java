package org.sopt.snappinserver.domain.review.repository;

import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    // 리뷰 목록 첫 페이지 조회 (cursor 없음)
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

    // 커서 이후 리뷰 목록 페이지 조회
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

    // 상품 기반 리뷰 수치(개수, 평균 별점) 조회
    @Query("""
            select new org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult(
                count(review),
                avg(review.rating)
            )
            from Review review
            join review.reservation reservation
            where reservation.product.id = :productId
        """)
    ProductReviewStatsResult findReviewStatsByProductId(
        @Param("productId") Long productId
    );
}


