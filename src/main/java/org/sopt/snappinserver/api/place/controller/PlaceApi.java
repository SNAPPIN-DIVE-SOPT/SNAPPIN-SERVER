package org.sopt.snappinserver.api.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.sopt.snappinserver.api.place.dto.response.GetPlaceListResponse;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "05 - Place", description = "장소 관련 API")
public interface PlaceApi {

    @Operation(
        summary = "촬영 장소 검색 API",
        description = "입력받은 키워드로 촬영 장소를 검색합니다."
    )
    ApiResponseBody<GetPlaceListResponse, Void> getPlaces(
        @NotBlank(message = "검색 키워드는 필수입니다.")
        @Length(max = 32, message = "검색 키워드는 최대 32자까지 입력할 수 있습니다.")
        @RequestParam String keyword
    );
}
