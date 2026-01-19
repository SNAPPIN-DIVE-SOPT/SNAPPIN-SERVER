package org.sopt.snappinserver.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CompleteReservationResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationCompleteUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatchReservationCompleteService implements PatchReservationCompleteUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public CompleteReservationResult completeReservation(
        Long userId,
        Long reservationId
    ) {
        Reservation reservation = getReservation(reservationId);
        validateReservationPhotographer(userId, reservation);

        reservation.completeShooting();

        return new CompleteReservationResult(
            reservation.getId(),
            reservation.getReservationStatus()
        );
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(
                () -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND)
            );
    }

    private void validateReservationPhotographer(Long userId, Reservation reservation) {
        if (!reservation.isReservationPhotographer(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
        }
    }
}
