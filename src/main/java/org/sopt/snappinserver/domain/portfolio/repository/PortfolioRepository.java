package org.sopt.snappinserver.domain.portfolio.repository;

import java.util.List;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    @Query(value = """
            SELECT p.id
            FROM portfolio p
            JOIN portfolio_mood pm ON p.id = pm.portfolio_id
            WHERE pm.mood_id IN (:moodIds)
            GROUP BY p.id
            HAVING COUNT(DISTINCT pm.mood_id) = :matchCount
            ORDER BY RANDOM()
            LIMIT :limit
        """, nativeQuery = true)
    List<Long> findIdsByMoodMatchCount(
        @Param("moodIds") List<Long> moodIds,
        @Param("matchCount") int matchCount,
        @Param("limit") int limit
    );

    @Query("""
            SELECT p
            FROM Portfolio p
            WHERE p.id IN :ids
        """)
    List<Portfolio> findAllByIdIn(@Param("ids") List<Long> ids);
}
