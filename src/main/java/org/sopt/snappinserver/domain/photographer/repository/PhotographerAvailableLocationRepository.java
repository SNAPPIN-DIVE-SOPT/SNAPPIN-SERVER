package org.sopt.snappinserver.domain.photographer.repository;

import java.util.List;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerAvailableLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotographerAvailableLocationRepository
    extends JpaRepository<PhotographerAvailableLocation, Long> {

    List<PhotographerAvailableLocation> findAllByPhotographer(Photographer photographer);
}
