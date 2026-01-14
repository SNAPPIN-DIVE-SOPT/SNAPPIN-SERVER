package org.sopt.snappinserver.api.home.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetRandomPhotographersResult;
import org.sopt.snappinserver.domain.place.service.dto.response.GetRecommendationPlaceResult;

@Schema(description = "스냅 명소 및 작가 추천 목록 조회 응답 DTO")
public record GetPlacePhotographerRecommendationResponse(

    @Schema(description = "추천 장소 목록")
    List<GetPlaceInfoResponse> places,

    @Schema(description = "추천 작가 목록")
    List<GetPhotographerInfoResponse> photographers
) {

    public static GetPlacePhotographerRecommendationResponse of(
        List<GetRecommendationPlaceResult> placeResults,
        List<GetRandomPhotographersResult> photographersResults
    ) {
        return new GetPlacePhotographerRecommendationResponse(
            placeResults.stream()
                .map(GetPlaceInfoResponse::from)
                .toList(),

            photographersResults.stream()
                .map(GetPhotographerInfoResponse::from)
                .toList()
        );
    }
}
