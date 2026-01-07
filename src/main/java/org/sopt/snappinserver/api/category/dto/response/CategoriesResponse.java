package org.sopt.snappinserver.api.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoriesResponse {

    private List<CategoryResponse> categories;

    public static CategoriesResponse from(List<CategoryResponse> categories) {
        return new CategoriesResponse(categories);
    }
}
