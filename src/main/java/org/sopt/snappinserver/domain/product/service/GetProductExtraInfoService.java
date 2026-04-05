package org.sopt.snappinserver.domain.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.sopt.snappinserver.domain.product.domain.entity.ProductOption;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductOptionRepository;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.AdditionalRequestSectionResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductExtraInfoResult;
import org.sopt.snappinserver.domain.product.service.dto.response.UploadConsentInfoResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductExtraInfoUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetProductExtraInfoService implements GetProductExtraInfoUseCase {

    private static final List<ProductOptionCategory> EXTRA_INFO_CATEGORIES = List.of(
        ProductOptionCategory.UPLOAD_AGREE_NOTE,
        ProductOptionCategory.UPLOAD_DISAGREE_NOTE,
        ProductOptionCategory.ADDITIONAL_REQUEST
    );

    private static final TypeReference<List<AdditionalRequestSectionResult>> ADDITIONAL_REQUEST_TYPE =
        new TypeReference<>() {};

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public GetProductExtraInfoResult getProductExtraInfo(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }

        List<ProductOption> options = productOptionRepository.findByProductIdAndProductOptionCategoryIn(
            productId,
            EXTRA_INFO_CATEGORIES
        );

        Map<ProductOptionCategory, String> byCategory = options.stream()
            .collect(
                Collectors.toMap(
                    ProductOption::getProductOptionCategory,
                    ProductOption::getAnswer,
                    (a, b) -> a
                )
            );

        UploadConsentInfoResult uploadConsent = new UploadConsentInfoResult(
            byCategory.get(ProductOptionCategory.UPLOAD_AGREE_NOTE),
            byCategory.get(ProductOptionCategory.UPLOAD_DISAGREE_NOTE)
        );

        List<AdditionalRequestSectionResult> additionalRequest =
            parseAdditionalRequest(byCategory.get(ProductOptionCategory.ADDITIONAL_REQUEST));

        return new GetProductExtraInfoResult(uploadConsent, additionalRequest);
    }

    private List<AdditionalRequestSectionResult> parseAdditionalRequest(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            List<AdditionalRequestSectionResult> parsed =
                objectMapper.readValue(json.strip(), ADDITIONAL_REQUEST_TYPE);
            if (parsed == null || parsed.isEmpty()) {
                return List.of();
            }
            return parsed.stream()
                .map(r -> new AdditionalRequestSectionResult(
                    r.title(),
                    r.content() == null ? List.of() : List.copyOf(r.content())
                ))
                .toList();
        } catch (JsonProcessingException e) {
            throw new ProductException(ProductErrorCode.INVALID_PRODUCT_OPTION_FORMAT);
        }
    }
}
