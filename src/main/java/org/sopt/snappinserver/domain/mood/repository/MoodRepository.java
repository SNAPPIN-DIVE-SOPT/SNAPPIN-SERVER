package org.sopt.snappinserver.domain.mood.repository;

import java.util.List;
import lombok.NonNull;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    @Override
    @NonNull
    List<Mood> findAll();

    @Query(
        value = """
                SELECT *
                FROM mood
                ORDER BY RANDOM()
                LIMIT :limit
            """,
        nativeQuery = true
    )
    List<Mood> findRandom(@Param("limit") int limit);

}
