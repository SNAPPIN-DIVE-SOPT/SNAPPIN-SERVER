package org.sopt.snappinserver.api.curation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;

@Schema(description = "무드 큐레이션 단계별 질문/사진 조회 DTO")
public record GetCurationQuestionPhotosResponse(

    @Schema(description = "질문 내용 DTO")
    GetQuestionResponse question,

    @Schema(description = "관련 사진 DTO")
    List<GetPhotoResponse> photos
) {

    public static GetCurationQuestionPhotosResponse from(
        GetCurationQuestionResult getCurationQuestionResult
    ) {
        return new GetCurationQuestionPhotosResponse(
            GetQuestionResponse.from(getCurationQuestionResult.question()),
            getCurationQuestionResult.photos().stream()
                .map(GetPhotoResponse::from)
                .toList()
        );
    }
}
