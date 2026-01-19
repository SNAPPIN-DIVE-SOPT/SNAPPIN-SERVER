package org.sopt.snappinserver.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.reservation.service.dto.response.ReservationPriceResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.GetReservationPriceUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetReservationPriceService implements GetReservationPriceUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationPriceResult getReservationPrice(Long reservationId) {
        Reservation reservation = getReservation(reservationId);
        int price = reservation.getProduct().getPrice();

        return new ReservationPriceResult(reservation.getId(), price);
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() ->
                new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND)
            );
    }
}

