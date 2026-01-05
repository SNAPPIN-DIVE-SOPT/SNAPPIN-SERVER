package org.sopt.snappinserver.domain.curation.repository;

import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long> {

}
