package org.sopt.snappinserver.api.v1.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetProductInfoResult;

@Schema(description = "상품 응답 DTO")
public record GetPortfolioProductInfoResponse(

    @Schema(description = "상품 ID")
    Long id,

    @Schema(description = "상품 썸네일")
    String imageUrl,

    @Schema(description = "상품명")
    String title,

    @Schema(description = "리뷰 별점 평균")
    Double rate,

    @Schema(description = "리뷰 수")
    long reviewCount,

    @Schema(description = "작가명")
    String photographer,

    @Schema(description = "상품 기본 가격")
    int price,

    @Schema(description = "관련 상품 무드")
    List<String> moods
) {

    public static GetPortfolioProductInfoResponse from(GetProductInfoResult result) {
        return new GetPortfolioProductInfoResponse(
            result.id(),
            result.imageUrl(),
            result.title(),
            result.rate(),
            result.reviewCount(),
            result.photographer(),
            result.price(),
            result.moods()
        );
    }
}
