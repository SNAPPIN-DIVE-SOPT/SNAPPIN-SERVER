package org.sopt.snappinserver.api.home.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.place.service.dto.response.GetRecommendationPlaceResult;

@Schema(description = "추천 장소 응답 DTO")
public record GetPlaceInfoResponse(

    @Schema(description = "장소 ID")
    Long id,

    @Schema(description = "장소 이름")
    String name,

    @Schema(description = "장소 이미지 url")
    String imageUrl
) {

    public static GetPlaceInfoResponse from(GetRecommendationPlaceResult result) {
        return new GetPlaceInfoResponse(
            result.id(),
            result.name(),
            result.imageUrl()
        );
    }
}
