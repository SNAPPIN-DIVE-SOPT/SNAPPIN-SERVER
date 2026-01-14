package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.response.CancelReservationResult;

public interface PatchReservationCancelUseCase {

    CancelReservationResult cancelReservation(Long userId, Long reservationId);

}
