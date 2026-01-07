package org.sopt.snappinserver.api.category.controller;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.category.dto.response.CategoriesResponse;
import org.sopt.snappinserver.api.category.dto.response.CategoryResponse;
import org.sopt.snappinserver.global.enums.SnapCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@RestController
public class CategoryController implements CategoryApi {

    @Override
    public CategoriesResponse getCategories() {
        List<CategoryResponse> categories =
                Arrays.stream(SnapCategory.values())
                        .map(CategoryResponse::from)
                        .toList();

        return CategoriesResponse.from(categories);
    }
}
