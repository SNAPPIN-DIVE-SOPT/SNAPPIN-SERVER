package org.sopt.snappinserver.api.v1.home.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetRandomPhotographersResult;

@Schema(description = "추천 작가 응답 DTO")
public record GetRecommendationPhotographerInfoResponse(

    @Schema(description = "작가 ID")
    Long id,

    @Schema(description = "작가명")
    String name,

    @Schema(description = "작가 프로필 이미지")
    String profileImageUrl,

    @Schema(description = "신규 작가 여부")
    boolean isNew,

    @Schema(description = "작가 한 줄 소개")
    String bio,

    @Schema(description = "작가 촬영 상품 목록")
    List<String> specialties
) {

    public static GetRecommendationPhotographerInfoResponse from(
        GetRandomPhotographersResult result
    ) {
        return new GetRecommendationPhotographerInfoResponse(
            result.id(),
            result.name(),
            result.profileImageUrl(),
            result.isNew(),
            result.bio(),
            result.specialties()
        );
    }
}
