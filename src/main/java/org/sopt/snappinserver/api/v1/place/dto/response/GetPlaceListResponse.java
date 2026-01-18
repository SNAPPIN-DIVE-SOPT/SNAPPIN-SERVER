package org.sopt.snappinserver.api.v1.place.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.place.service.dto.response.GetPlaceListResult;

@Schema(description = "촬영 장소 목록 검색 API")
public record GetPlaceListResponse(

    @Schema(description = "검색된 촬영 장소 목록")
    List<GetPlaceResponse> places
) {

    public static GetPlaceListResponse from(GetPlaceListResult result) {
        return new GetPlaceListResponse(
            result.places().stream()
                .map(GetPlaceResponse::from)
                .toList()
        );
    }
}
