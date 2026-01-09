package org.sopt.snappinserver.domain.wish.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.wish.domain.entity.WishPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishPortfolioRepository extends JpaRepository<WishPortfolio, Long> {
    Optional<WishPortfolio> findByUserAndPortfolio(User user, Portfolio portfolio);
}
