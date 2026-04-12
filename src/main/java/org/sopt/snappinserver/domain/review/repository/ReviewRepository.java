package org.sopt.snappinserver.domain.review.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 리뷰 Spring Data 저장 - 상품·예약 혼합 조회·통계 등 공용 쿼리
 * 예약 전용·상품 전용 쓰기/예약 조회 경계는 ReservationReviewRepository,ProductReviewRepository가 위임
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByReservation_Id(Long reservationId);

    // 리뷰 목록 첫 페이지 조회 (cursor 없음)
    @Query("""
            select distinct review
            from Review review
            left join fetch review.user author
            left join fetch review.reservation reservation
            left join fetch reservation.user reservationUser
            left join reservation.product reservationProduct
            where (
                (review.product is not null and review.product.id = :productId)
                or (reservation is not null and reservationProduct.id = :productId)
            )
            order by review.id desc
        """)
    List<Review> findReviewsWithUserByProductId(
        @Param("productId") Long productId,
        Pageable pageable
    );

    // 커서 이후 리뷰 목록 페이지 조회
    @Query("""
            select distinct review
            from Review review
            left join fetch review.user author
            left join fetch review.reservation reservation
            left join fetch reservation.user reservationUser
            left join reservation.product reservationProduct
            where (
                (review.product is not null and review.product.id = :productId)
                or (reservation is not null and reservationProduct.id = :productId)
            )
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
            count(r),
            cast(round(avg(r.rating), 1) as double)
        )
        from Review r
        left join r.reservation res
        left join res.product resProd
        where (
            (r.product is not null and r.product.id = :productId)
            or (res is not null and resProd.id = :productId)
        )
        """)
    ProductReviewStatsResult findReviewStatsByProductId(
        @Param("productId") Long productId
    );

    // 상품 리뷰 통계 수치 여러 개 배치 조회
    @Query("""
        select
            case when r.product is not null then r.product.id else resProd.id end,
            new org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult(
                count(r),
                cast(round(avg(r.rating), 1) as double)
            )
        from Review r
        left join r.reservation res
        left join res.product resProd
        where (
            (r.product is not null and r.product.id in :productIds)
            or (res is not null and resProd.id in :productIds)
        )
        group by case when r.product is not null then r.product.id else resProd.id end
        """)
    List<Object[]> findReviewStatsByProductIds(@Param("productIds") List<Long> productIds);

    // 예약 기준 리뷰 존재 여부
    @Query("""
            select r.reservation.id
            from Review r
            where r.reservation.id in :reservationIds
        """)
    List<Long> findReviewedReservationIds(
        @Param("reservationIds") List<Long> reservationIds
    );

    Optional<Review> findFirstByReservationOrderByIdDesc(Reservation reservation);
}
