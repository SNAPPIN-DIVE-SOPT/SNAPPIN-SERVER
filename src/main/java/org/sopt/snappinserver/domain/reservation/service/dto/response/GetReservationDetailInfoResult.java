package org.sopt.snappinserver.domain.reservation.service.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record GetReservationDetailInfoResult(
    String client,
    LocalDateTime createdAt,
    LocalDate date,
    LocalTime startTime,
    int durationMinutes,
    String place,
    int peopleCount,
    String requestNote
) {

}
