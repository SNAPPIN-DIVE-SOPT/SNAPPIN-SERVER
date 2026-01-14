package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.response.GetProductResult;

public interface GetProductDetailUseCase {

    GetProductResult getProductDetail(Long userId, Long productId);
}
