package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.response.ProductDurationTimeResult;

public interface GetProductDurationTimeUseCase {
    ProductDurationTimeResult getProductDurationTime(Long productId);

}
