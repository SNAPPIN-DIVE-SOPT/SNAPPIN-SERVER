package org.sopt.snappinserver.domain.photographer.repository;

import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotographerRepository extends JpaRepository<Photographer, Long> {

}
