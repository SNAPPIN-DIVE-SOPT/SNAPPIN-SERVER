package org.sopt.snappinserver.domain.wish.service.dto.response;

public record WishProductResult(Long productId, Boolean liked) {

    public static WishProductResult of(Long productId, Boolean liked) {
        return new WishProductResult(productId, liked);
    }
}
