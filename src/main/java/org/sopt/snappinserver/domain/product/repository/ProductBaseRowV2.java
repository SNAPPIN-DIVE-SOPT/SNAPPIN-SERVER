package org.sopt.snappinserver.domain.product.repository;

public record ProductBaseRowV2(
    Long id,
    String imageUrl,
    boolean isLiked,
    Long likeCount,
    Double averageRating,
    String title,
    Long reviewCount,
    String photographerName,
    Integer price
) {

}
