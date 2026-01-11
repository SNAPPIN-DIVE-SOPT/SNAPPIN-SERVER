package org.sopt.snappinserver.api.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "상품 예약 요청 DTO")
public record ProductReservationRequest(

    @Schema(description = "촬영 희망 날짜", example = "2026-03-15")
    @NotNull LocalDate date,

    @Schema(description = "촬영 시작 시간", example = "10:00")
    @NotNull LocalTime startTime,

    @Schema(description = "촬영 시간 (시간 단위)", example = "2")
    @NotNull Integer durationTime,

    @Schema(description = "촬영 장소 아이디", example = "1")
    @NotNull Long placeId,

    @Schema(description = "촬영 인원", example = "2")
    @NotNull Integer peopleCount,

    @Schema(description = "기타 요청 사항", example = "단체 사진 위주로 촬영 요청드립니다.", nullable = true)
    String requestNote
) {

}
