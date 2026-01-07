package org.sopt.snappinserver.api.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "촬영 상황 카테고리 목록 응답 DTO")
public class CategoriesResponse {

    @Schema(description = "촬영 상황 카테고리 목록")
    private List<CategoryResponse> categories;

    public static CategoriesResponse from(List<CategoryResponse> categories) {
        return new CategoriesResponse(categories);
    }
}
