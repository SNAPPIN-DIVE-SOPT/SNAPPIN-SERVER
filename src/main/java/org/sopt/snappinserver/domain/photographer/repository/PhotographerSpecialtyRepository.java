package org.sopt.snappinserver.domain.photographer.repository;

import java.util.List;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotographerSpecialtyRepository
    extends JpaRepository<PhotographerSpecialty, Long> {

    List<PhotographerSpecialty> findAllByPhotographer(Photographer photographer);
}
