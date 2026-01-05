package org.sopt.snappinserver.domain.reservation.repository;

import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
