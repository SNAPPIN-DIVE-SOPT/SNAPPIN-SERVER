package org.sopt.snappinserver.domain.portfolio.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioPhotoRepository extends JpaRepository<PortfolioPhoto, Long> {

    Optional<PortfolioPhoto> findFirstByPortfolioOrderByDisplayOrderAsc(Portfolio portfolio);

    @Query("""
        SELECT pp
        FROM PortfolioPhoto pp
        JOIN FETCH pp.photo
        WHERE pp.portfolio.id IN :portfolioIds
    """)
    List<PortfolioPhoto> findByPortfolioIds(@Param("portfolioIds") List<Long> portfolioIds);

}
