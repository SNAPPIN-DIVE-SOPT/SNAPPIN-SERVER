package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailResult;

public interface GetReservationDetailUseCase {

    GetReservationDetailResult getReservationDetail(
        Long userId,
        Long reservationId
    );
}
