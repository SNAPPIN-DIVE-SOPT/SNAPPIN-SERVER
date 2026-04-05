package org.sopt.snappinserver.domain.product.repository;

import java.util.List;
import java.util.Map;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQuery;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQueryV2;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResult;
import org.sopt.snappinserver.domain.product.service.dto.response.PopularMoodProductItemResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResultV2;

public interface ProductRepositoryCustom {

    LikeStatusProjection findLikeStatus(Long productId, Long userId);

    List<GetProductCardResult> findProducts(
        GetProductListQuery query,
        Map<MoodCategory, List<Long>> moodGroupMap
    );

    List<GetProductCardResultV2> findProductCardsV2(
        GetProductListQueryV2 query,
        Map<MoodCategory, List<Long>> moodGroupMap,
        int size
    );

    List<String> findProductMoods(Long productId);

    List<Long> findTopProductIdsByMoodOrderByWishCount(Long moodId, int limit);

    List<PopularMoodProductItemResult> findPopularMoodProductItemsByIds(List<Long> productIds);

}
