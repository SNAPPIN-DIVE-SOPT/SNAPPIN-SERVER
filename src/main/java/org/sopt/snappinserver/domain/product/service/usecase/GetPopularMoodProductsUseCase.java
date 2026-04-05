package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.response.GetPopularMoodProductsResult;

public interface GetPopularMoodProductsUseCase {

    GetPopularMoodProductsResult getPopularMoodProducts(Long moodId);
}
