package org.sopt.snappinserver.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.entity.ProductOption;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductOptionRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductDurationTimeResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductDurationTimeUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductDurationTimeService
    implements GetProductDurationTimeUseCase {

    private final ProductOptionRepository productOptionRepository;

    @Override
    public ProductDurationTimeResult getProductDurationTime(Long productId) {
        ProductOption option = productOptionRepository
            .findByProductIdAndProductOptionCategory(
                productId,
                ProductOptionCategory.DURATION_TIME
            )
            .orElseThrow(() ->
                new ProductException(ProductErrorCode.PRODUCT_DURATION_TIME_NOT_FOUND)
            );

        int durationMinutes = parseDurationTime(option);
        validateDurationTime(durationMinutes);

        double durationHours = convertMinutesToHours(durationMinutes);

        return new ProductDurationTimeResult(durationHours);
    }


    private int parseDurationTime(ProductOption option) {
        String answer = option.getAnswer();

        if (answer == null || answer.isBlank()) {
            throw new ProductException(
                ProductErrorCode.INVALID_PRODUCT_DURATION_TIME
            );
        }

        try {
            return Integer.parseInt(option.getAnswer());
        } catch (NumberFormatException e) {
            throw new ProductException(
                ProductErrorCode.INVALID_PRODUCT_OPTION_FORMAT
            );
        }
    }

    private void validateDurationTime(int durationTime) {
        if (durationTime <= 0) {
            throw new ProductException(
                ProductErrorCode.INVALID_PRODUCT_DURATION_TIME
            );
        }
    }

    private double convertMinutesToHours(int minutes) {
        return minutes / 60.0;
    }
}
