package org.sopt.snappinserver.domain.portfolio.repository;

import java.util.List;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioMood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioMoodRepository extends JpaRepository<PortfolioMood, Long> {

    @Query("""
            SELECT pm
            FROM PortfolioMood pm
            JOIN FETCH pm.mood
            WHERE pm.portfolio.id IN :portfolioIds
        """)
    List<PortfolioMood> findByPortfolioIds(
        @Param("portfolioIds") List<Long> portfolioIds
    );

}
