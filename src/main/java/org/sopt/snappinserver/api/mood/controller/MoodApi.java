package org.sopt.snappinserver.api.mood.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.snappinserver.api.mood.dto.response.GetMoodFilterListResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;

@Tag(name = "05 - Mood", description = "무드 관련 API")
public interface MoodApi {

    @Operation(
        summary = "전체 무드 필터 값 조회 API",
        description = "전체 무드 필터 값을 해당 무드 카테고리, 사용자 큐레이션 진행 여부와 함께 반환합니다."
    )
    ApiResponseBody<GetMoodFilterListResponse, Void> getAllMoodFilters(
        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );
}
