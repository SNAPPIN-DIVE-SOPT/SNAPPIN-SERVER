package org.sopt.snappinserver.api.photographer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerProfileResult;

@Schema(description = "작가 상세 조회 응답 DTO")
public record PhotographerProfileResponse(

    @Schema(description = "작가 ID", example = "1")
    Long id,

    @Schema(description = "작가가 설정한 작가명", example = "스윙스냅")
    String name,

    @Schema(description = "작가 한 줄 소개", example = "일상의 아름다움을 포착합니다")
    String bio,

    @Schema(description = "촬영 상품", example = "[ \"졸업스냅\", \"웨딩스냅\" ]")
    List<String> specialties,

    @Schema(description = "활동 지역", example = "[ \"서울\", \"인천\" ]")
    List<String> locations
) {

    public static PhotographerProfileResponse from(GetPhotographerProfileResult result) {
        return new PhotographerProfileResponse(
            result.id(),
            result.name(),
            result.bio(),
            result.specialties(),
            result.locations()
        );
    }
}
