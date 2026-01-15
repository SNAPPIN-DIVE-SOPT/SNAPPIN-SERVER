package org.sopt.snappinserver.domain.product.repository;

import java.util.List;
import java.util.Map;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQuery;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResult;

public interface ProductRepositoryCustom {

    LikeStatusProjection findLikeStatus(Long productId, Long userId);

    List<GetProductCardResult> findProducts(
        GetProductListQuery query,
        Map<MoodCategory, List<Long>> moodGroupMap
    );
}
