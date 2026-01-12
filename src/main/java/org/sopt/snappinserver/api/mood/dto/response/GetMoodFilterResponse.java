package org.sopt.snappinserver.api.mood.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterResult;

@Schema(description = "무드 필터 응답 DTO")
public record GetMoodFilterResponse(

    @Schema(description = "무드 ID")
    Long id,

    @Schema(description = "해당 무드가 속해있는 카테고리")
    String category,

    @Schema(description = "무드 이름")
    String name,

    @Schema(description = "사용자 무드 큐레이션 결과 여부")
    boolean isCurated
) {

    public static GetMoodFilterResponse from(GetMoodFilterResult result) {
        return new GetMoodFilterResponse(
            result.id(),
            result.category(),
            result.name(),
            result.isCurated()
        );
    }
}
