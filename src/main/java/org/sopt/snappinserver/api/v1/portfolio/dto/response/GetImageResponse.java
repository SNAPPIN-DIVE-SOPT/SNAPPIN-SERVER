package org.sopt.snappinserver.api.v1.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetImageResult;

@Schema(description = "포트폴리오 이미지 응답 DTO")
public record GetImageResponse(

    @Schema(description = "이미지 URL")
    String imageUrl,

    @Schema(description = "이미지 표시 순서")
    int order
) {

    public static GetImageResponse from(GetImageResult result) {
        return new GetImageResponse(result.imageUrl(), result.order());
    }
}
