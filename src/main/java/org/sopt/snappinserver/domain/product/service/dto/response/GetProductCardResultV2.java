package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;

public record GetProductCardResultV2(
    Long id,
    String imageUrl,
    boolean isLiked,
    long likeCount,
    Double averageRating,
    String title,
    long reviewCount,
    String photographer,
    int price,
    List<String> moods
) {

}
