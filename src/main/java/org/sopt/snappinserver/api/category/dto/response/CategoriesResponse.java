package org.sopt.snappinserver.api.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "촬영 상황 카테고리 목록 응답 DTO")
public record CategoriesResponse(

    @Schema(description = "촬영 상황 카테고리 목록")
    List<CategoryResponse> categories

) {
    public static CategoriesResponse from(List<CategoryResponse> categories) {
        return new CategoriesResponse(categories);
    }
}
