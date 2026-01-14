package org.sopt.snappinserver.domain.product.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerAvailableLocation;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSpecialty;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerAvailableLocationRepository;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerSpecialtyRepository;
import org.sopt.snappinserver.domain.place.domain.entity.AvailableLocation;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepositoryCustom;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductAvailableLocation;
import org.sopt.snappinserver.domain.product.domain.entity.ProductMood;
import org.sopt.snappinserver.domain.product.domain.entity.ProductOption;
import org.sopt.snappinserver.domain.product.domain.entity.ProductPhoto;
import org.sopt.snappinserver.domain.product.domain.enums.ProductOptionCategory;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductAvailableLocationRepository;
import org.sopt.snappinserver.domain.product.repository.ProductMoodRepository;
import org.sopt.snappinserver.domain.product.repository.ProductOptionRepository;
import org.sopt.snappinserver.domain.product.repository.ProductPhotoRepository;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.GetPhotographerInfoResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductInfoResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductDetailUseCase;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.sopt.snappinserver.global.enums.SnapCategory;
import org.sopt.snappinserver.global.s3.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetProductDetailService implements GetProductDetailUseCase {

    private final ProductRepository productRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final ProductOptionRepository productOptionRepository;
    private final PortfolioRepositoryCustom portfolioRepositoryCustom;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;
    private final ProductMoodRepository productMoodRepository;
    private final ProductAvailableLocationRepository productAvailableLocationRepository;
    private final PhotographerSpecialtyRepository photographerSpecialtyRepository;
    private final PhotographerAvailableLocationRepository photographerAvailableLocationRepository;

    public GetProductResult getProductDetail(Long userId, Long productId) {
        Product product = getProduct(productId);
        List<String> productPhotos = getProductPhotos(product);
        LikeStatusProjection likeStatus = getLikeStatus(userId, productId);
        ProductReviewStatsResult reviewStats = reviewRepository
            .findReviewStatsByProductId(productId);

        List<ProductAvailableLocation> availableLocations = productAvailableLocationRepository
            .findByProduct(product);
        List<ProductMood> productMoods = productMoodRepository.findAllByProduct(product);

        Photographer photographer = product.getPhotographer();
        List<String> specialties = getSpecialties(photographer);
        List<String> locations = getLocations(photographer);

        List<ProductOption> productOptions = productOptionRepository.findByProduct(product);
        Map<ProductOptionCategory, String> optionMap = getProductOptions(productOptions);

        GetPhotographerInfoResult photographerInfoResult = getPhotographerInfoResult(
            photographer,
            specialties,
            locations
        );
        GetProductInfoResult productInfoResult = getProductInfoResult(
            product,
            availableLocations,
            productMoods,
            optionMap
        );

        return GetProductResult.of(
            product,
            productPhotos,
            likeStatus,
            reviewStats,
            photographerInfoResult,
            productInfoResult
        );
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    private List<String> getProductPhotos(Product product) {
        return productPhotoRepository.findByProduct(product).stream()
            .map(ProductPhoto::getPhoto)
            .map(Photo::getImageUrl)
            .map(s3Service::getPresignedUrl)
            .toList();
    }

    private LikeStatusProjection getLikeStatus(Long userId, Long productId) {
        return portfolioRepositoryCustom.findLikeStatus(productId,
            userId);
    }

    private static Map<ProductOptionCategory, String> getProductOptions(
        List<ProductOption> productOptions) {
        return productOptions.stream()
            .collect(
                Collectors.toMap(
                    ProductOption::getProductOptionCategory,
                    ProductOption::getAnswer
                )
            );
    }

    private List<String> getSpecialties(Photographer photographer) {
        return photographerSpecialtyRepository
            .findAllByPhotographer(photographer)
            .stream()
            .map(PhotographerSpecialty::getSpecialty)
            .map(SnapCategory::getCategory)
            .toList();
    }

    private List<String> getLocations(Photographer photographer) {
        return photographerAvailableLocationRepository
            .findAllByPhotographer(photographer)
            .stream()
            .map(PhotographerAvailableLocation::getAvailableLocation)
            .map(AvailableLocation::getFullLocation)
            .toList();
    }

    private static GetPhotographerInfoResult getPhotographerInfoResult(Photographer photographer,
        List<String> specialties, List<String> locations) {
        return GetPhotographerInfoResult.of(
            photographer,
            specialties,
            locations
        );
    }

    private static GetProductInfoResult getProductInfoResult(
        Product product,
        List<ProductAvailableLocation> availableLocations,
        List<ProductMood> productMoods,
        Map<ProductOptionCategory, String> optionMap
    ) {
        return GetProductInfoResult.of(
            product,
            availableLocations,
            productMoods,
            optionMap
        );
    }

}
