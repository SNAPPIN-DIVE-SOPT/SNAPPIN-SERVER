package org.sopt.snappinserver.api.v2.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetPhotographerInfoResult;

@Schema(description = "상품 조회 시 작가 응답 DTO V2")
public record GetProductPhotographerInfoResponseV2(

    @Schema(description = "작가 ID")
    Long id,

    @Schema(description = "작가 이름")
    String name,

    @Schema(description = "프로필 이미지 URL")
    String profileImageUrl,

    @Schema(description = "한줄 소개")
    String bio,

    @Schema(description = "연락 링크 URL", example = "https://open.kakao.com/o/example")
    String contactLink,

    @Schema(description = "촬영 상품 (전문 스냅 유형)")
    List<String> specialties,

    @Schema(description = "활동 지역")
    List<String> locations
) {

    public static GetProductPhotographerInfoResponseV2 from(GetPhotographerInfoResult result) {
        return new GetProductPhotographerInfoResponseV2(
            result.id(),
            result.name(),
            result.profileImageUrl(),
            result.bio(),
            result.contactLink(),
            result.specialties(),
            result.locations()
        );
    }
}
