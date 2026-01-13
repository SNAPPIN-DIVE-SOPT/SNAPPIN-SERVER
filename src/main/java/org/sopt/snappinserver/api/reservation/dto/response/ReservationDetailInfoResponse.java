package org.sopt.snappinserver.api.reservation.dto.response;

import java.time.format.DateTimeFormatter;

public record ReservationDetailInfoResponse(
    String client,
    String createdAt,
    String date,
    String startTime,
    double durationTime,
    String place,
    int peopleCount,
    String requestNote
) {
    public static ReservationDetailInfoResponse from(
        org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailInfoResult result
    ) {
        return new ReservationDetailInfoResponse(
            result.client(),
            result.createdAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            result.date().toString(),
            result.startTime().toString(),
            result.durationMinutes() / 60.0,
            result.place(),
            result.peopleCount(),
            result.requestNote()
        );
    }
}
