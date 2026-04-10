package org.sopt.snappinserver.api.v1.curation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;

@Schema(description = "무드 큐레이션 단계별 질문/사진 조회 DTO")
public record GetCurationQuestionPhotosResponse(

    @Schema(description = "관련 사진 DTO")
    List<GetPhotoResponse> photos
) {

    public static GetCurationQuestionPhotosResponse from(
        GetCurationQuestionResult getCurationQuestionResult
    ) {
        return new GetCurationQuestionPhotosResponse(
            getCurationQuestionResult.photos().stream()
                .map(GetPhotoResponse::from)
                .toList()
        );
    }
}
