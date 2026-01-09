package org.sopt.snappinserver.domain.mood.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MoodVectorRepositoryImpl implements MoodVectorRepository {

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<MoodWithScore> findTopCandidates(String embedding) {
        String sql = """
                SELECT m.id as id,
                       m.name as name,
                       (1 - (m.embedding <=> cast(:embedding as vector))) as score
                FROM mood m
                WHERE m.embedding IS NOT NULL
                ORDER BY m.embedding <=> cast(:embedding as vector)
                LIMIT 10
            """;

        List<Tuple> tuples = (List<Tuple>) em.createNativeQuery(sql, Tuple.class)
            .setParameter("embedding", embedding)
            .getResultList();

        return tuples.stream()
            .map(t -> (MoodWithScore) new MoodWithScoreImpl(
                    ((Number) t.get("id")).longValue(),
                    t.get("name", String.class),
                    t.get("score", Number.class).floatValue()
                )
            )
            .toList();
    }
}