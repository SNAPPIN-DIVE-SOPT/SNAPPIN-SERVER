package org.sopt.snappinserver.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPriceResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductPriceUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductPriceService implements GetProductPriceUseCase {

    private final ProductRepository productRepository;

    @Override
    public ProductPriceResult getProductPrice(Long productId) {
        int price = getPrice(productId);

        return new ProductPriceResult(price);
    }

    private int getPrice(Long productId) {
        return productRepository.findProductPriceById(productId)
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }
}
