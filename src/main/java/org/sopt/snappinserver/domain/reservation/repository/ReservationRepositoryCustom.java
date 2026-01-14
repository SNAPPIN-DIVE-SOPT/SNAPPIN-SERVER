package org.sopt.snappinserver.domain.reservation.repository;

import java.util.List;
import org.sopt.snappinserver.domain.place.domain.entity.Place;

public interface ReservationRepositoryCustom {

    List<Place> findTop5MostReservedPlacesInLastMonth();
}
