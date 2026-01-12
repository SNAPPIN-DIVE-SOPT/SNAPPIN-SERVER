package org.sopt.snappinserver.api.mood.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterListResult;

@Schema(description = "전체 무드 필터 값 조회 응답 DTO")
public record GetMoodFilterListResponse(

    @Schema(description = "무드 값 목록")
    List<GetMoodFilterResponse> moods
) {


    public static GetMoodFilterListResponse from(GetMoodFilterListResult result) {
        return new GetMoodFilterListResponse(
            result.moods().stream()
                .map(GetMoodFilterResponse::from)
                .toList()
        );
    }
}
