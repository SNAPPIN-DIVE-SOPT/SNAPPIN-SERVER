package org.sopt.snappinserver.domain.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByProductAndReservedAtBetweenAndReservationStatusIn(
        Product product,
        LocalDateTime startOfDay,
        LocalDateTime endOfDay,
        List<ReservationStatus> statuses
    );

    List<Reservation> findAllByProductAndReservationStatusIn(
        Product product,
        List<ReservationStatus> statuses
    );
}
