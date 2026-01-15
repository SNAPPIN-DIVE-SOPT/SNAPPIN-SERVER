package org.sopt.snappinserver.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.reservation.service.dto.response.RefuseReservationResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationRefuseUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatchReservationRefuseService
    implements PatchReservationRefuseUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public RefuseReservationResult refuseReservation(Long photographerId, Long reservationId) {
        Reservation reservation = getReservation(reservationId);
        validateReservationPhotographer(photographerId, reservation);

        reservation.refuse();

        return new RefuseReservationResult(reservation.getId(), reservation.getReservationStatus());
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ReservationException(
                ReservationErrorCode.RESERVATION_NOT_FOUND
            ));
    }

    private static void validateReservationPhotographer(Long photographerId, Reservation reservation) {
        if (!reservation.isReservationPhotographer(photographerId)) {
            throw new ReservationException(
                ReservationErrorCode.RESERVATION_USER_NOT_MATCH
            );
        }
    }
}
