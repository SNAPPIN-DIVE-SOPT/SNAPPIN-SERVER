package org.sopt.snappinserver.domain.mood.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import lombok.NonNull;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    @Override
    @NonNull
    List<Mood> findAll();

    @Query(
        value = """
              SELECT *
              FROM mood
              WHERE id >= (
                SELECT FLOOR(RANDOM() * (SELECT MAX(id) FROM mood))
              )
              LIMIT :limit
            """,
        nativeQuery = true
    )
    List<Mood> findRandom(@Param("limit") int limit);

    @Query(value = """
        SELECT p.*
        FROM portfolio p
        JOIN portfolio_mood pm ON p.id = pm.portfolio_id
        WHERE pm.mood_id IN (:moodIds)
        GROUP BY p.id
        HAVING COUNT(DISTINCT pm.mood_id) = :matchCount
        ORDER BY RANDOM()
        LIMIT :limit
    """, nativeQuery = true)
    List<Portfolio> findByMoodMatchCount(
        @Param("moodIds") List<Long> moodIds,
        @Param("matchCount") int matchCount,
        @Param("limit") int limit
    );
}
