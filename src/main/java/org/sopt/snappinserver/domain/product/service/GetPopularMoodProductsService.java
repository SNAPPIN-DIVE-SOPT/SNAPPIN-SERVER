package org.sopt.snappinserver.domain.product.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.domain.exception.MoodErrorCode;
import org.sopt.snappinserver.domain.mood.domain.exception.MoodException;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.product.repository.ProductRepositoryCustom;
import org.sopt.snappinserver.domain.wish.repository.WishProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.GetPopularMoodProductsResult;
import org.sopt.snappinserver.domain.product.service.dto.response.PopularMoodProductItemResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetPopularMoodProductsUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPopularMoodProductsService implements GetPopularMoodProductsUseCase {

    private static final int TOP_BY_WISH_COUNT = 12;
    private static final int RANDOM_PICK = 4;

    private final MoodRepository moodRepository;
    private final ProductRepositoryCustom productRepository;
    private final WishProductRepository wishProductRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetPopularMoodProductsResult getPopularMoodProducts(Long moodId, Long userId) {
        Mood mood = moodRepository.findById(moodId)
            .orElseThrow(() -> new MoodException(MoodErrorCode.MOOD_NOT_FOUND));

        List<Long> topIds = productRepository.findTopProductIdsByMoodOrderByWishCount(
            moodId,
            TOP_BY_WISH_COUNT
        );

        List<Long> pickedIds = pickRandomSubset(topIds);
        List<PopularMoodProductItemResult> items =
            productRepository.findPopularMoodProductItemsByIds(pickedIds);

        Set<Long> likedProductIds = resolveLikedProductIds(userId, pickedIds);

        List<PopularMoodProductItemResult> withUrls = items.stream()
            .map(item -> new PopularMoodProductItemResult(
                item.id(),
                toFullImageUrl(item.imageUrl()),
                item.title(),
                item.rate(),
                item.reviewCount(),
                item.photographer(),
                item.price(),
                likedProductIds.contains(item.id())
            ))
            .toList();

        return new GetPopularMoodProductsResult(mood.getName(), withUrls);
    }

    private Set<Long> resolveLikedProductIds(Long userId, List<Long> productIds) {
        if (userId == null || productIds.isEmpty()) {
            return Set.of();
        }
        return new HashSet<>(wishProductRepository.findProductIdsByUserIdAndProductIdIn(
            userId,
            productIds
        ));
    }

    private String toFullImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }
        return cloudFrontDomain + imageUrl;
    }

    private static List<Long> pickRandomSubset(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        List<Long> copy = new ArrayList<>(ids);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(RANDOM_PICK, copy.size()));
    }
}
