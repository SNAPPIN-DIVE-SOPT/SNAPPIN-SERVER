package org.sopt.snappinserver.api.curation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodCurationResult;

@Schema(description = "무드 큐레이션 결과 응답 DTO")
public record CreateMoodCurationResponse(

    @Schema(description = "사용자 이름", example = "김소연")
    String userName,

    @Schema(description = "무드 큐레이션 결과 목록")
    List<CreateMoodResponse> moods
) {

    public static CreateMoodCurationResponse from(CreateMoodCurationResult result) {
        return new CreateMoodCurationResponse(
            result.name(),
            result.moods().stream().map(CreateMoodResponse::from).toList()
        );
    }
}
