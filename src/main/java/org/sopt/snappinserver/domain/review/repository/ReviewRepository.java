package org.sopt.snappinserver.domain.review.repository;

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
        select r
        from Review r
        join fetch r.reservation res
        join fetch res.user u
        where res.product.id = :productId
        order by r.id desc
    """)
    List<Review> findTop6WithUserByProductId(
        @Param("productId") Long productId
    );

    // 커서 이후 페이지 조회
    @Query("""
        select r
        from Review r
        join fetch r.reservation res
        join fetch res.user u
        where res.product.id = :productId
          and r.id < :cursor
        order by r.id desc
    """)
    List<Review> findTop6WithUserByProductIdAndCursor(
        @Param("productId") Long productId,
        @Param("cursor") Long cursor
    );
}
