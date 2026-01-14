package org.sopt.snappinserver.domain.portfolio.service.mapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPhotographerInfoResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioDetailResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetProductInfoResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.PortfolioDetailProjection;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.sopt.snappinserver.global.s3.S3Service;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PortfolioDetailMapper {

    private final S3Service s3Service;

    public GetPortfolioDetailResult toResult(
        PortfolioDetailProjection portfolio,
        LikeStatusProjection likeStatus,
        List<String> portfolioImageUrls,
        List<String> portfolioMoods,
        Photographer photographer,
        List<String> photographerSpecialties,
        List<String> photographerLocations,
        Product product,
        String productThumbnailUrl,
        ProductReviewStatsResult reviewStats,
        List<String> productMoods
    ) {

        List<String> presignedPortfolioImages =
            portfolioImageUrls.stream()
                .map(s3Service::getPresignedUrl)
                .toList();

        String presignedProductThumbnail =
            s3Service.getPresignedUrl(productThumbnailUrl);

        return new GetPortfolioDetailResult(
            portfolio.portfolioId(),
            portfolio.description(),
            presignedPortfolioImages,
            likeStatus.liked(),
            likeStatus.likeCount(),
            portfolio.snapCategory().getCategory(),
            portfolio.placeName(),
            portfolio.startsAt(),
            portfolioMoods,
            GetPhotographerInfoResult.of(
                photographer,
                photographerSpecialties,
                photographerLocations
            ),
            GetProductInfoResult.of(
                product,
                presignedProductThumbnail,
                reviewStats,
                productMoods
            )
        );
    }
}

