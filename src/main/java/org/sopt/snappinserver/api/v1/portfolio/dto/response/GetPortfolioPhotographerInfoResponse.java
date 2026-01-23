package org.sopt.snappinserver.api.v1.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPhotographerInfoResult;

@Schema(description = "포트폴리오 작가 응답 DTO")
public record GetPortfolioPhotographerInfoResponse(

    @Schema(description = "작가 ID")
    Long id,

    @Schema(description = "작가명")
    String name,

    @Schema(description = "작가 프로필 이미지")
    String imageUrl,

    @Schema(description = "작가 한줄 소개")
    String bio,

    @Schema(description = "작가 촬영 상품 종류")
    List<String> specialties,

    @Schema(description = "작가 활동 지역")
    List<String> locations
) {

    public static GetPortfolioPhotographerInfoResponse from(
        GetPhotographerInfoResult result
    ) {
        return new GetPortfolioPhotographerInfoResponse(
            result.id(),
            result.name(),
            result.imageUrl(),
            result.bio(),
            result.specialties(),
            result.locations()
        );
    }
}
