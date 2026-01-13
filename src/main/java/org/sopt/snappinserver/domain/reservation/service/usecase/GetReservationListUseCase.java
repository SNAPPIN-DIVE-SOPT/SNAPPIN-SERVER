package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatusTab;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListResult;

public interface GetReservationListUseCase {

    GetReservationListResult getReservationList(
        Long userId,
        ReservationStatusTab tab
    );
}
