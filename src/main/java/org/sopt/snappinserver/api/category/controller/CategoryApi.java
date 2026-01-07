package org.sopt.snappinserver.api.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.snappinserver.api.category.dto.response.CategoriesResponse;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "02 - Category", description = "촬영 상황 관련 API")
public interface CategoryApi {

    @Operation(
            summary = "촬영 상황 조회",
            description = "촬영 상황 옵션으로 사용될 스냅 유형 전체 목록을 조회합니다."
    )
    @GetMapping
    CategoriesResponse getCategories();
}
