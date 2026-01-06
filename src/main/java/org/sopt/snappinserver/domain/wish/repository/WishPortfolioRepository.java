package org.sopt.snappinserver.domain.wish.repository;

import org.sopt.snappinserver.domain.wish.domain.entity.WishPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishPortfolioRepository extends JpaRepository<WishPortfolio, Long> {

}
