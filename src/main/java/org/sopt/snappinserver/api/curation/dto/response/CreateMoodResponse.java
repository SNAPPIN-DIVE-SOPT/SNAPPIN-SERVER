package org.sopt.snappinserver.api.curation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodResult;

@Schema(description = "사용자 맞춤 무드 결과 DTO")
public record CreateMoodResponse(

    @Schema(description = "무드 태그 id")
    Long id,

    @Schema(description = "무드 태그 이름")
    String name
) {

    public static CreateMoodResponse from(CreateMoodResult result) {
        return new CreateMoodResponse(result.id(), result.name());
    }
}
