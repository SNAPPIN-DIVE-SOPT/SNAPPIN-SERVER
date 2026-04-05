package org.sopt.snappinserver.domain.wish.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.wish.domain.entity.WishPortfolio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishPortfolioRepository extends JpaRepository<WishPortfolio, Long> {

    Optional<WishPortfolio> findByUserAndPortfolio(User user, Portfolio portfolio);

    List<WishPortfolio> findAllByUserOrderByCreatedAtDesc(User user);

    @Query("""
            select wp
            from WishPortfolio wp
            join fetch wp.portfolio p
            where wp.user = :user
            order by wp.id desc
        """)
    List<WishPortfolio> findAllByUserWithPortfolioOrderByIdDesc(
        @Param("user") User user,
        Pageable pageable
    );

    @Query("""
            select wp
            from WishPortfolio wp
            join fetch wp.portfolio p
            where wp.user = :user
              and wp.id < :cursor
            order by wp.id desc
        """)
    List<WishPortfolio> findAllByUserWithPortfolioOrderByIdDescAndCursor(
        @Param("user") User user,
        @Param("cursor") Long cursor,
        Pageable pageable
    );

    @Query("""
            select wp.portfolio.id, count(wp)
            from WishPortfolio wp
            where wp.portfolio.id in :portfolioIds
            group by wp.portfolio.id
        """)
    List<Object[]> countGroupedByPortfolioId(@Param("portfolioIds") Collection<Long> portfolioIds);
}
