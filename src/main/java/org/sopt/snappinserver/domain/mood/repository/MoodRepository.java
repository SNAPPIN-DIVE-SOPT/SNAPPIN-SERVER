package org.sopt.snappinserver.domain.mood.repository;

import com.pgvector.PGvector;
import java.util.List;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    // Hibernate 6의 벡터 지원을 통해 처리됩니다.
    @Query(value = """
            SELECT m.id as id, 
                   m.name as name, 
                   (1 - (m.embedding <=> cast(:embedding as vector))) as score
            FROM mood m
            ORDER BY m.embedding <=> cast(:embedding as vector) ASC
            LIMIT 10
        """, nativeQuery = true)
    List<MoodWithScore> findCandidates(@Param("embedding") List<Float> embedding);

    interface MoodWithScore {

        Long getId();

        String getName();

        Float getScore();
    }
}
