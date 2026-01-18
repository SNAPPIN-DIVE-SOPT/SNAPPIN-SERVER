package org.sopt.snappinserver.api.v1.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioDetailResult;

@Schema(description = "포트폴리오 상세 조회 응답 DTO")
public record GetPortfolioDetailResponse(

    @Schema(description = "포트폴리오 ID")
    Long id,

    @Schema(description = "포트폴리오 한줄 소개")
    String description,

    @Schema(description = "이미지 URL 목록")
    List<String> images,

    @Schema(description = "좋아요 여부")
    boolean isLiked,

    @Schema(description = "좋아요 수")
    int likeCount,

    @Schema(description = "촬영 종류")
    String snapCategory,

    @Schema(description = "촬영 장소")
    String place,

    @Schema(description = "촬영 시각")
    String startsAt,

    @Schema(description = "스냅 무드")
    List<String> moods,

    @Schema(description = "작가 프로필")
    GetPortfolioPhotographerInfoResponse photographerInfo,

    @Schema(description = "관련 상품 정보")
    GetProductInfoResponse productInfo
) {

    public static GetPortfolioDetailResponse from(GetPortfolioDetailResult result) {
        return new GetPortfolioDetailResponse(
            result.id(),
            result.description(),
            result.images(),
            result.isLiked(),
            result.likeCount(),
            result.snapCategory(),
            result.place(),
            result.startsAt(),
            result.moods(),
            GetPortfolioPhotographerInfoResponse.from(result.photographerInfo()),
            GetProductInfoResponse.from(result.productInfo())
        );
    }
}
