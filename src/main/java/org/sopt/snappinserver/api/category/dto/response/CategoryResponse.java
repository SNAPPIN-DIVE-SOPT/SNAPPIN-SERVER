package org.sopt.snappinserver.api.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    private String key;
    private String label;

    public static CategoryResponse from(SnapCategory category) {
        return new CategoryResponse(
                category.name(),
                category.getCategory()
        );
    }
}
