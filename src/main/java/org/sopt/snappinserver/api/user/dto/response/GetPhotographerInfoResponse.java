package org.sopt.snappinserver.api.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.user.service.dto.response.GetPhotographerInfoResult;

@Schema(description = "작가 정보 응답 DTO")
public record GetPhotographerInfoResponse(

    @Schema(description = "작가명")
    String name,

    @Schema(description = "한줄 소개")
    String bio,

    @Schema(description = "작가 촬영 상품")
    List<String> specialties,

    @Schema(description = "작가 활동 지역")
    List<String> locations
) {

    public static GetPhotographerInfoResponse from(GetPhotographerInfoResult result) {
        return new GetPhotographerInfoResponse(
            result.name(),
            result.bio(),
            result.specialties(),
            result.locations()
        );
    }
}
