package org.sopt.snappinserver.global.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "에러 발생 시 meta DTO")
public record ErrorMeta(
    @Schema(description = "에러가 발생한 API 경로입니다.")
    String path,

    @Schema(description = "에러가 발생한 시간입니다.")
    Instant timestamp
) {

}
