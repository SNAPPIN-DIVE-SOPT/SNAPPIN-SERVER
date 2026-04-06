package org.sopt.snappinserver.domain.product.service.dto.response;

public record PopularMoodProductItemResult(
    Long id,
    String imageUrl,
    String title,
    Double rate,
    long reviewCount,
    String photographer,
    int price,
    boolean isLiked
) {

}
