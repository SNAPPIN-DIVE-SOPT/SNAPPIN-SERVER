package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;

public record GetProductInfoResult(
    Long id,
    String imageUrl,
    String title,
    Double rate,
    long reviewCount,
    String photographer,
    int price,
    List<String> moods
) {

    public static GetProductInfoResult of(
        Product product,
        String productImageUrl,
        ProductReviewStatsResult productReviewStatsResult,
        List<String> productMoods
        ) {
        return new GetProductInfoResult(
            product.getId(),
            productImageUrl,
            product.getTitle(),
            productReviewStatsResult.averageRating(),
            productReviewStatsResult.reviewCount(),
            product.getPhotographer().getNickname(),
            product.getPrice(),
            productMoods
        );
    }
}
