package org.sopt.snappinserver.domain.product.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.entity.ProductOption;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductOptionRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductPeopleRangeUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProductPeopleRangeService implements GetProductPeopleRangeUseCase {

    private final ProductOptionRepository productOptionRepository;

    @Override
    public ProductPeopleRangeResult getProductPeopleRange(Long productId) {

        List<ProductOption> options = getPeopleRangeOptions(productId);

        int minPeople = getMinPeople(options);
        int maxPeople = getMaxPeople(options);

        return new ProductPeopleRangeResult(minPeople, maxPeople);
    }

    private List<ProductOption> getPeopleRangeOptions(Long productId) {
        return productOptionRepository.findByProductIdAndProductOptionCategoryIn(
            productId,
            List.of(
                ProductOptionCategory.MIN_PEOPLE,
                ProductOptionCategory.MAX_PEOPLE
            )
        );
    }

    private int getMinPeople(List<ProductOption> options) {
        return options.stream()
            .filter(option ->
                option.getProductOptionCategory() == ProductOptionCategory.MIN_PEOPLE
            )
            .mapToInt(option -> Integer.parseInt(option.getAnswer()))
            .findFirst()
            .orElse(1);
    }

    private int getMaxPeople(List<ProductOption> options) {
        return options.stream()
            .filter(option ->
                option.getProductOptionCategory() == ProductOptionCategory.MAX_PEOPLE
            )
            .mapToInt(option -> Integer.parseInt(option.getAnswer()))
            .findFirst()
            .orElseThrow(() ->
                new ProductException(ProductErrorCode.PRODUCT_PEOPLE_RANGE_NOT_FOUND)
            );
    }


}