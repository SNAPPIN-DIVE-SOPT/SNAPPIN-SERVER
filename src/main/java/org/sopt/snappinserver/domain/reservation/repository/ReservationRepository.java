package org.sopt.snappinserver.domain.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    // 고객 예약 목록
    @Query("""
        select r
        from Reservation r
        join fetch r.product p
        join fetch p.photographer ph
        join fetch r.user u
        where u.id = :userId
          and r.reservationStatus in :statuses
        order by r.updatedAt desc
    """)
    List<Reservation> findClientReservations(
        @Param("userId") Long userId,
        @Param("statuses") List<ReservationStatus> statuses
    );

    // 작가 예약 목록
    @Query("""
        select r
        from Reservation r
        join fetch r.product p
        join fetch p.photographer ph
        join fetch r.user u
        where ph.user.id = :userId
          and r.reservationStatus in :statuses
        order by r.updatedAt desc
    """)
    List<Reservation> findPhotographerReservations(
        @Param("userId") Long userId,
        @Param("statuses") List<ReservationStatus> statuses
    );
}
