package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;

public interface GetProductPeopleRangeUseCase {
    ProductPeopleRangeResult getProductPeopleRange(Long productId);
}
