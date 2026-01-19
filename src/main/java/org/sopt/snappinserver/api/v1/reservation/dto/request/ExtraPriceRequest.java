package org.sopt.snappinserver.api.v1.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "촬영 추가 비용 DTO")
public record ExtraPriceRequest(

    @Schema(description = "비용명", example = "원본 JPG 추가")
    String name,

    @Schema(description = "금액", example = "10000")
    @Positive Integer amount
) {

}
