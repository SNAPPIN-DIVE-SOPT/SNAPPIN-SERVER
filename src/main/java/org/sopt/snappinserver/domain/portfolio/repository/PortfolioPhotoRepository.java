package org.sopt.snappinserver.domain.portfolio.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioPhotoRepository extends JpaRepository<PortfolioPhoto, Long> {

    Optional<PortfolioPhoto> findFirstByPortfolioOrderByDisplayOrderAsc(Portfolio portfolio);
}
