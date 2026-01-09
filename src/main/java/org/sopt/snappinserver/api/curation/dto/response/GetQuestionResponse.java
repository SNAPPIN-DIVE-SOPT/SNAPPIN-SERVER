package org.sopt.snappinserver.api.curation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetQuestionResult;

@Schema(description = "질문 내용 DTO")
public record GetQuestionResponse(

    @Schema(description = "질문 id", example = "1")
    Long id,

    @Schema(description = "질문 내용", example = "어떤 장소 무드를 선호하시나요?")
    String contents,

    @Schema(description = "질문 단계", example = "1")
    Integer step
) {

    public static GetQuestionResponse from(GetQuestionResult getQuestionResult) {
        return new GetQuestionResponse(
            getQuestionResult.id(),
            getQuestionResult.contents(),
            getQuestionResult.step()
        );
    }
}
