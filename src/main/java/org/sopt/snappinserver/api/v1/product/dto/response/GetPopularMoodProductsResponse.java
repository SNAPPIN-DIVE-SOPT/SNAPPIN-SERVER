package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetPopularMoodProductsResult;

@Schema(description = "인기 무드 상품 목록 응답 DTO")
public record GetPopularMoodProductsResponse(

    @Schema(description = "무드 태그 이름")
    String mood,

    @Schema(description = "상품 목록")
    List<GetPopularMoodProductItemResponse> products
) {

    public static GetPopularMoodProductsResponse from(GetPopularMoodProductsResult result) {
        return new GetPopularMoodProductsResponse(
            result.mood(),
            result.products().stream()
                .map(GetPopularMoodProductItemResponse::from)
                .toList()
        );
    }
}
