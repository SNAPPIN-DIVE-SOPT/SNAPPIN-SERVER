package org.sopt.snappinserver.domain.curation.repository;

import java.util.List;
import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long> {

    List<Curation> findTop3ByUserOrderByRankAscCreatedAtDesc(User user);
}
