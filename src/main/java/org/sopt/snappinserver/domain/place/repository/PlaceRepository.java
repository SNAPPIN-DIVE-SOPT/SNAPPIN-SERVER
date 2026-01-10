package org.sopt.snappinserver.domain.place.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.place.domain.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String name);
}
