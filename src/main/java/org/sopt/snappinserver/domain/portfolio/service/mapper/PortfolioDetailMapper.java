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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PortfolioDetailMapper {

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

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
                .filter(str -> str != null && !str.isBlank())
                .map(str -> cloudFrontDomain + str)
                .toList();

        String presignedProductThumbnail = (productThumbnailUrl != null)
            ? cloudFrontDomain + productThumbnailUrl
            : null;

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
                cloudFrontDomain + photographer.getUser().getProfileImageUrl(),
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

