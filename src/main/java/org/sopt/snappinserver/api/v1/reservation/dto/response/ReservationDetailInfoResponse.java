package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailInfoResult;

@Schema(description = "예약 상세 정보 DTO")
public record ReservationDetailInfoResponse(

    @Schema(description = "예약자명", example = "홍길동")
    String client,

    @Schema(description = "예약 생성 일시", example = "2026-02-18 10:05")
    String createdAt,

    @Schema(description = "촬영 희망 날짜", example = "2026-03-15")
    String date,

    @Schema(description = "촬영 시작 시간", example = "10:00")
    String startTime,

    @Schema(description = "촬영 시간", example = "1.5")
    Double durationTime,

    @Schema(description = "촬영 장소", example = "건국대")
    String place,

    @Schema(description = "촬영 인원", example = "2")
    int peopleCount,

    @Schema(description = "기타 요청 사항", example = "예쁘게 찍어주세요.")
    String requestNote
) {
    private static final DateTimeFormatter CREATED_AT_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
