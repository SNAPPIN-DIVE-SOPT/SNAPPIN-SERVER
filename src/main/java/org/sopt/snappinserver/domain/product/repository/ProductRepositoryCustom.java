package org.sopt.snappinserver.domain.product.repository;

import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;

public interface ProductRepositoryCustom {

    LikeStatusProjection findLikeStatus(Long productId, Long userId);
}
