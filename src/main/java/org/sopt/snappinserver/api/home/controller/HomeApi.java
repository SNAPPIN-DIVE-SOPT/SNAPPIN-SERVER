package org.sopt.snappinserver.api.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.snappinserver.api.home.dto.response.GetPlacePhotographerRecommendationResponse;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;

@Tag(name = "04 - Home", description = "홈 화면에서 사용되는 API")
public interface HomeApi {

    @Operation(
        summary = "스냅 명소 및 작가 추천 목록 조회 API",
        description = "최근 1개월 간 예약 건수가 가장 많은 장소 5곳과 랜덤으로 작가 5명을 추천합니다."
    )
    ApiResponseBody<GetPlacePhotographerRecommendationResponse, Void> getRecommendation();

}
