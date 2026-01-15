package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.response.ProductPriceResult;

public interface GetProductPriceUseCase {
    ProductPriceResult getProductPrice(Long productId);
}
