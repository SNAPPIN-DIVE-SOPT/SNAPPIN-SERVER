package org.sopt.snappinserver.api.v1.place.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.place.service.dto.response.GetPlaceResult;

@Schema(description = "촬영 장소 검색 응답 DTO")
public record GetPlaceResponse(

    @Schema(description = "촬영 장소 ID", example = "1")
    Long id,

    @Schema(description = "촬영 장소명", example = "건국대학교")
    String name
) {

    public static GetPlaceResponse from(GetPlaceResult result) {
        return new GetPlaceResponse(result.id(), result.name());
    }
}
