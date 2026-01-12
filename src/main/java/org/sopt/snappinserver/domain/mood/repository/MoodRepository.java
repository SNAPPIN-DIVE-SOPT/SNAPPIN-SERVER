package org.sopt.snappinserver.domain.mood.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import lombok.NonNull;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
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
}
