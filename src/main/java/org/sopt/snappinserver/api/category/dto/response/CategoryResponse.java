package org.sopt.snappinserver.api.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Getter
@AllArgsConstructor
@Schema(description = "촬영 상황 카테고리 응답 DTO")
public class CategoryResponse {

    @Schema(description = "카테고리 키(영문)", example = "GRADUATION")
    private String key;
    @Schema(description = "카테고리 라벨(한글)", example = "졸업스냅")
    private String label;

    public static CategoryResponse from(SnapCategory category) {
        return new CategoryResponse(
                category.name(),
                category.getCategory()
        );
    }
}
