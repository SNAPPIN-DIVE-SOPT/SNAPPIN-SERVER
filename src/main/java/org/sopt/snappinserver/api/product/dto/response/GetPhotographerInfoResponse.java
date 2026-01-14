package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetPhotographerInfoResult;

@Schema(description = "상품 조회 시 작가 응답 DTO")
public record GetPhotographerInfoResponse(

    @Schema(description = "작가 ID")
    Long id,

    @Schema(description = "작가 이름")
    String name,

    @Schema(description = "한줄 소개")
    String bio,

    @Schema(description = "촬영 상품 (전문 스냅 유형)")
    List<String> specialties,

    @Schema(description = "활동 지역")
    List<String> locations
) {

    public static GetPhotographerInfoResponse from(GetPhotographerInfoResult result) {
        return new GetPhotographerInfoResponse(
            result.id(),
            result.name(),
            result.bio(),
            result.specialties(),
            result.locations()
        );
    }
}
