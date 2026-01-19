package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.response.ReservationPriceResult;

public interface GetReservationPriceUseCase {
    ReservationPriceResult getReservationPrice(Long reservationId);
}
