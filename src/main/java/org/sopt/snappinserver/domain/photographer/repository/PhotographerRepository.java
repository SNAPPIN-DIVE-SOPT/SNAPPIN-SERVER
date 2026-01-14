package org.sopt.snappinserver.domain.photographer.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotographerRepository extends JpaRepository<Photographer, Long> {

    Optional<Photographer> findByUser(User user);

    boolean existsByUser(User user);

    @Query(
        value = """
                SELECT *
                FROM photographer
                ORDER BY RANDOM()
                LIMIT :limit
            """,
        nativeQuery = true
    )
    List<Photographer> findRandom(@Param("limit") int limit);
}
