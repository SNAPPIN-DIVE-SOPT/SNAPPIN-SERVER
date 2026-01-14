package org.sopt.snappinserver.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CancelReservationResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationCancelUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatchReservationCancelService implements PatchReservationCancelUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public CancelReservationResult cancelReservation(Long userId, Long reservationId) {
        Reservation reservation = getReservation(reservationId);

        validateReservationClient(userId, reservation);
        ReservationStatus previousStatus = reservation.getReservationStatus();

        reservation.cancel();

        return new CancelReservationResult(
            reservation.getId(),
            previousStatus,
            reservation.getReservationStatus()
        );
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
            () -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND)
        );
    }

    private void validateReservationClient(Long userId, Reservation reservation) {
        if (!reservation.isReservationClient(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
        }
    }
}
