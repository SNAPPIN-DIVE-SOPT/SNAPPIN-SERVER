package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.response.PayReservationResult;

public interface PatchReservationPayUseCase {

    PayReservationResult payReservation(Long userId, Long reservationId);
}
