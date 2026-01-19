package org.sopt.snappinserver.api.v1.curation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetAllCurationQuestionResult;

@Schema(description = "전체 무드 큐레이션 질문 / 사진 조회 API 응답 DTO")
public record GetAllCurationQuestionsResponse(

    @Schema(description = "질문 별 내용 및 사진 모음 목록")
    List<GetCurationQuestionPhotosResponse> questions
) {

    public static GetAllCurationQuestionsResponse from(
        GetAllCurationQuestionResult result
    ) {
        return new GetAllCurationQuestionsResponse(
            result.questions().stream()
                .map(GetCurationQuestionPhotosResponse::from)
                .toList()
        );
    }
}
