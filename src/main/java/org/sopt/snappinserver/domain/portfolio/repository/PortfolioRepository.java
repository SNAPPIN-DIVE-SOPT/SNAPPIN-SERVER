package org.sopt.snappinserver.domain.portfolio.repository;

import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

}
