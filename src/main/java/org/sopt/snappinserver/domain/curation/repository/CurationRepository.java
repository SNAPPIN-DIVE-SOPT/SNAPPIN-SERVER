package org.sopt.snappinserver.domain.curation.repository;

import java.util.List;
import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long> {

    @Query("""
            select c from Curation c
            where c.user = :user
            order by c.createdAt desc
        """)
    List<Curation> findLatestByUser(User user, Pageable pageable);
}
