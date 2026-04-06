package org.sopt.snappinserver.domain.wish.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.wish.domain.entity.WishProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishProductRepository extends JpaRepository<WishProduct, Long> {

    @Query("""
            select wp.product.id
            from WishProduct wp
            where wp.user.id = :userId
              and wp.product.id in :productIds
        """)
    List<Long> findProductIdsByUserIdAndProductIdIn(
        @Param("userId") Long userId,
        @Param("productIds") Collection<Long> productIds
    );

    Optional<WishProduct> findByUserAndProduct(User user, Product product);

    @Query("""
            select wp
            from WishProduct wp
            join fetch wp.product p
            join fetch p.photographer
            where wp.user = :user
            order by wp.createdAt desc
        """)
    List<WishProduct> findAllByUserWithProductOrderByCreatedAtDesc(
        @Param("user") User user
    );

    @Query("""
            select wp
            from WishProduct wp
            join fetch wp.product p
            join fetch p.photographer
            where wp.user = :user
            order by wp.id desc
        """)
    List<WishProduct> findAllByUserWithProductOrderByIdDesc(
        @Param("user") User user,
        Pageable pageable
    );

    @Query("""
            select wp
            from WishProduct wp
            join fetch wp.product p
            join fetch p.photographer
            where wp.user = :user
              and wp.id < :cursor
            order by wp.id desc
        """)
    List<WishProduct> findAllByUserWithProductOrderByIdDescAndCursor(
        @Param("user") User user,
        @Param("cursor") Long cursor,
        Pageable pageable
    );
}
