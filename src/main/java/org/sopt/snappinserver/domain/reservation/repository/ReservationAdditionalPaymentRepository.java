package org.sopt.snappinserver.domain.reservation.repository;

import java.util.List;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.entity.ReservationAdditionalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationAdditionalPaymentRepository
    extends JpaRepository<ReservationAdditionalPayment, Long> {

    List<ReservationAdditionalPayment> findAllByReservation(Reservation reservation);
}
