package org.sopt.snappinserver.domain.product.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.product.repository.ProductRepositoryCustom;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQuery;
import org.sopt.snappinserver.domain.product.service.dto.response.CursorMeta;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductListUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetProductListService implements GetProductListUseCase {

    private static final int PAGE_SIZE = 10;

    private final ProductRepositoryCustom productRepository;
    private final MoodRepository moodRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    public GetProductListResult getProductList(GetProductListQuery query) {
        Map<MoodCategory, List<Long>> moodGroupMap = groupByCategory(query.moodIds());
        List<GetProductCardResult> results = productRepository.findProducts(query, moodGroupMap);

        List<GetProductCardResult> resultsWithPresignedUrl =
            results.stream()
                .map(result -> {
                    String presignedUrl =
                        (result.imageUrl() != null && !result.imageUrl().isBlank())
                            ? cloudFrontDomain + result.imageUrl()
                            : null;

                    return new GetProductCardResult(
                        result.id(),
                        presignedUrl,
                        result.title(),
                        result.rate(),
                        result.reviewCount(),
                        result.photographer(),
                        result.price(),
                        result.moods()
                    );
                })
                .toList();

        boolean hasNext = resultsWithPresignedUrl.size() > PAGE_SIZE;
        if (hasNext) {
            resultsWithPresignedUrl = resultsWithPresignedUrl.subList(0, PAGE_SIZE);
        }

        Long nextCursor = resultsWithPresignedUrl.isEmpty()
            ? null
            : resultsWithPresignedUrl.get(resultsWithPresignedUrl.size() - 1).id();

        return new GetProductListResult(
            resultsWithPresignedUrl,
            new CursorMeta(nextCursor, hasNext)
        );
    }

    private Map<MoodCategory, List<Long>> groupByCategory(List<Long> moodIds) {
        if (moodIds == null || moodIds.isEmpty()) {
            return Map.of();
        }

        return moodRepository.findAllById(moodIds).stream()
            .collect(
                Collectors.groupingBy(
                    Mood::getCategory,
                    Collectors.mapping(
                        Mood::getId,
                        toList()
                    )
                )
            );
    }
}

