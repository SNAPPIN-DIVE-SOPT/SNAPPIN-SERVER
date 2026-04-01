package org.sopt.snappinserver.api.v2.mood.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterResult;

@Schema(description = "무드 필터 응답 DTO")
public record GetMoodFilterResponse(

    @Schema(description = "무드 ID")
    Long id,

    @Schema(description = "무드 이름")
    String name
) {

    public static GetMoodFilterResponse from(GetMoodFilterResult result) {
        return new GetMoodFilterResponse(
            result.id(),
            result.name()
        );
    }
}
