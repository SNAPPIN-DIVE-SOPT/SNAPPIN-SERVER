package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.response.ConfirmReservationResult;

public interface PatchReservationConfirmUseCase {

    ConfirmReservationResult confirmReservation(Long photographerId, Long reservationId);
}
