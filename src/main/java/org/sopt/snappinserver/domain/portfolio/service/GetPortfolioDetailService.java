package org.sopt.snappinserver.domain.portfolio.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.portfolio.domain.exception.PortfolioErrorCode;
import org.sopt.snappinserver.domain.portfolio.domain.exception.PortfolioException;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepository;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepositoryCustom;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioDetailResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.PortfolioDetailProjection;
import org.sopt.snappinserver.domain.portfolio.service.mapper.PortfolioDetailMapper;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPortfolioDetailUseCase;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPortfolioDetailService implements GetPortfolioDetailUseCase {

    private final PortfolioRepositoryCustom queryRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final PortfolioDetailMapper mapper;
    private final PortfolioRepository portfolioRepository;

    @Override
    public GetPortfolioDetailResult findPortfolioDetail(Long userId, Long portfolioId) {
        validateExistsPortfolio(portfolioId);
        PortfolioDetailProjection portfolioProjection = queryRepository.findDetail(portfolioId);
        LikeStatusProjection likeStatus = queryRepository.findLikeStatus(portfolioId, userId);

        List<String> portfolioImages = queryRepository.findPortfolioImageUrls(portfolioId);
        List<String> portfolioMoods = queryRepository.findPortfolioMoods(portfolioId);
        List<String> photographerSpecialties = queryRepository.findPhotographerSpecialties(
            portfolioProjection.photographerId()
        );
        List<String> photographerLocations = queryRepository.findPhotographerAvailableLocations(
            portfolioProjection.photographerId()
        );

        Product product = getProduct(portfolioProjection);
        Photographer photographer = product.getPhotographer();
        ProductReviewStatsResult reviewStats = reviewRepository.findReviewStatsByProductId(
            product.getId()
        );
        String productThumbnail = queryRepository.findProductThumbnailUrl(product.getId());
        List<String> productMoods = queryRepository.findProductMoods(product.getId());

        return mapper.toResult(
            portfolioProjection,
            likeStatus,
            portfolioImages,
            portfolioMoods,
            photographer,
            photographerSpecialties,
            photographerLocations,
            product,
            productThumbnail,
            reviewStats,
            productMoods
        );
    }

    private void validateExistsPortfolio(Long portfolioId) {
        if (!portfolioRepository.existsById(portfolioId)) {
            throw new PortfolioException(PortfolioErrorCode.PORTFOLIO_NOT_FOUND);
        }
    }

    private Product getProduct(PortfolioDetailProjection detail) {
        return productRepository.findById(detail.productId())
            .orElseThrow(() -> new PortfolioException(PortfolioErrorCode.PRODUCT_NOT_FOUND));
    }
}
