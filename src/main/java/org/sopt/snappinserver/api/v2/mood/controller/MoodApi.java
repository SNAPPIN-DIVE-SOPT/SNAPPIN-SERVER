package org.sopt.snappinserver.api.v2.mood.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.snappinserver.api.v2.mood.dto.response.GetMoodFilterListResponse;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "05 - Mood", description = "무드 관련 API")
public interface MoodApi {

    @Operation(
        summary = "전체 무드 필터 값 조회 API",
        description = "전체 무드 필터 값 목록을 반환합니다."
    )
    @GetMapping
    ApiResponseBody<GetMoodFilterListResponse, Void> getAllMoodFilters();
}
