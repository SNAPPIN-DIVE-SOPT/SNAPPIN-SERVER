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
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQueryV2;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResultV2;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListMetaV2;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResultV2;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductListUseCaseV2;
import org.sopt.snappinserver.global.enums.SortType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetProductListServiceV2 implements GetProductListUseCaseV2 {

    private static final int PAGE_SIZE = 10;

    private final ProductRepositoryCustom productRepositoryCustom;
    private final MoodRepository moodRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetProductListResultV2 getProductList(GetProductListQueryV2 query) {
        Map<MoodCategory, List<Long>> moodGroupMap = groupByCategory(query.moodIds());

        List<GetProductCardResultV2> rows = productRepositoryCustom
            .findProductCardsV2(query, moodGroupMap, PAGE_SIZE).stream()
            .map(result -> {
                String imageUrl = (result.imageUrl() != null && !result.imageUrl().isBlank())
                    ? cloudFrontDomain + result.imageUrl()
                    : null;
                return new GetProductCardResultV2(
                    result.id(), imageUrl, result.isLiked(), result.likeCount(),
                    result.averageRating(), result.title(), result.reviewCount(),
                    result.photographer(), result.price(), result.moods()
                );
            })
            .toList();

        boolean hasNext = rows.size() > PAGE_SIZE;
        List<GetProductCardResultV2> products = hasNext ? rows.subList(0, PAGE_SIZE) : rows;
        String nextCursor = hasNext
            ? buildNextCursor(products.get(products.size() - 1), query.sort())
            : null;

        return new GetProductListResultV2(products, new GetProductListMetaV2(hasNext, nextCursor));
    }

    private String buildNextCursor(GetProductCardResultV2 last, SortType sort) {
        SortType resolved = sort == null ? SortType.RECOMMENDED : sort;
        return switch (resolved) {
            case LATEST -> String.valueOf(last.id());
            case POPULAR -> last.likeCount() + ":" + last.id();
            case RECOMMENDED -> last.averageRating() + ":" + last.id();
        };
    }

    private Map<MoodCategory, List<Long>> groupByCategory(List<Long> moodIds) {
        if (moodIds == null || moodIds.isEmpty()) {
            return Map.of();
        }

        return moodRepository.findAllById(moodIds).stream()
            .collect(
                Collectors.groupingBy(
                    Mood::getCategory,
                    Collectors.mapping(Mood::getId, toList())
                )
            );
    }
}
