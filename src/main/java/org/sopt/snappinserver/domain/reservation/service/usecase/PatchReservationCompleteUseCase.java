package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.response.CompleteReservationResult;

public interface PatchReservationCompleteUseCase {

    CompleteReservationResult completeReservation(
        Long photographerId,
        Long reservationId
    );
}
