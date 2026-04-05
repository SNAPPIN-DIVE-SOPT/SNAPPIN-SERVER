package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.product.domain.entity.Product;

public record GetProductResult(
    Long id,
    List<String> images,
    String title,
    Boolean isLiked,
    long likeCount,
    Double averageRate,
    long reviewCount,
    int price,
    GetPhotographerInfoResult photographerInfo,
    GetProductInfoResult productInfo
) {

    public static GetProductResult of(
        Product product,
        List<String> images,
        LikeStatusProjection likeStatus,
        ProductReviewStatsResult reviewStats,
        GetPhotographerInfoResult getPhotographerInfoResult,
        GetProductInfoResult getProductInfoResult
    ) {
        return new GetProductResult(
            product.getId(),
            images,
            product.getTitle(),
            likeStatus.liked(),
            likeStatus.likeCount() != null ? likeStatus.likeCount() : 0L,
            reviewStats.averageRating(),
            reviewStats.reviewCount(),
            product.getPrice(),
            getPhotographerInfoResult,
            getProductInfoResult
        );
    }
}
