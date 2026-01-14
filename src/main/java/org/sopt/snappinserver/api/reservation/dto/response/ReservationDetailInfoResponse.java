package org.sopt.snappinserver.api.reservation.dto.response;

import java.time.format.DateTimeFormatter;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailInfoResult;

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
    private static final DateTimeFormatter CREATED_AT_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ReservationDetailInfoResponse from(GetReservationDetailInfoResult result) {
        return new ReservationDetailInfoResponse(
            result.client(),
            result.createdAt().format(CREATED_AT_FORMATTER),
            result.date().toString(),
            result.startTime().toString(),
            result.durationMinutes() / 60.0,
            result.place(),
            result.peopleCount(),
            result.requestNote()
        );
    }
}
