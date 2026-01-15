package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.response.RefuseReservationResult;

public interface PatchReservationRefuseUseCase {

    RefuseReservationResult refuseReservation(Long photographerId, Long reservationId);
}
