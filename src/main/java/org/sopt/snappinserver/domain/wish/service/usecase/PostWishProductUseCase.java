package org.sopt.snappinserver.domain.wish.service.usecase;

import org.sopt.snappinserver.domain.wish.service.dto.response.WishProductResult;

public interface PostWishProductUseCase {
    WishProductResult toggleProductWish(Long userId, Long productId);
}
