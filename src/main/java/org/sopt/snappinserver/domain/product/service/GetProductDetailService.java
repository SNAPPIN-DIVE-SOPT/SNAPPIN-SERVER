package org.sopt.snappinserver.domain.product.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerAvailableLocation;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSpecialty;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerAvailableLocationRepository;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerSpecialtyRepository;
import org.sopt.snappinserver.domain.place.domain.entity.AvailableLocation;
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
import org.sopt.snappinserver.domain.product.repository.ProductRepositoryCustom;
import org.sopt.snappinserver.domain.product.service.dto.response.GetPhotographerInfoResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductInfoResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductDetailUseCase;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.sopt.snappinserver.global.enums.SnapCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetProductDetailService implements GetProductDetailUseCase {

    private final ProductRepository productRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final ReviewRepository reviewRepository;
    private final ProductMoodRepository productMoodRepository;
    private final ProductAvailableLocationRepository productAvailableLocationRepository;
    private final PhotographerSpecialtyRepository photographerSpecialtyRepository;
    private final PhotographerAvailableLocationRepository photographerAvailableLocationRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    public GetProductResult getProductDetail(Long userId, Long productId) {
        Product product = getProduct(productId);
        List<String> productPhotos = getProductPhotos(product);
        LikeStatusProjection likeStatus = getLikeStatus(userId, productId);
        ProductReviewStatsResult reviewStats = reviewRepository
            .findReviewStatsByProductId(productId);

        List<ProductAvailableLocation> availableLocations = productAvailableLocationRepository
            .findByProduct(product);
        List<ProductMood> productMoods = productMoodRepository.findAllByProductOrderById(product);

        Photographer photographer = product.getPhotographer();
        List<String> specialties = getSpecialties(photographer);
        List<String> locations = getLocations(photographer);

        List<ProductOption> productOptions = productOptionRepository.findByProduct(product);
        Map<ProductOptionCategory, String> optionMap = getProductOptions(productOptions);
        String profileImageUrl = cloudFrontDomain + photographer.getUser().getProfileImageUrl();

        GetPhotographerInfoResult photographerInfoResult = getPhotographerInfoResult(
            photographer,
            profileImageUrl,
            specialties,
            locations
        );
        GetProductInfoResult productInfoResult = getProductInfoResult(
            product,
            availableLocations,
            productMoods,
            optionMap,
            productOptions
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
        return productPhotoRepository.findByProductOrderByDisplayOrderAsc(product).stream()
            .map(ProductPhoto::getPhoto)
            .filter(photo ->
                photo != null && photo.getImageUrl() != null && !photo.getImageUrl().isBlank()
            )
            .map(photo -> cloudFrontDomain + photo.getImageUrl())
            .toList();
    }

    private LikeStatusProjection getLikeStatus(Long userId, Long productId) {
        return productRepositoryCustom.findLikeStatus(productId,
            userId);
    }

    private static Map<ProductOptionCategory, String> getProductOptions(
        List<ProductOption> productOptions) {
        return productOptions.stream()
            .collect(
                Collectors.toMap(
                    ProductOption::getProductOptionCategory,
                    ProductOption::getAnswer,
                    (existing, replacement) -> existing
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

    private static GetPhotographerInfoResult getPhotographerInfoResult(
        Photographer photographer,
        String photographerImageUrl,
        List<String> specialties,
        List<String> locations
    ) {
        return GetPhotographerInfoResult.of(
            photographer,
            photographerImageUrl,
            specialties,
            locations
        );
    }

    private static GetProductInfoResult getProductInfoResult(
        Product product,
        List<ProductAvailableLocation> availableLocations,
        List<ProductMood> productMoods,
        Map<ProductOptionCategory, String> optionMap,
        List<ProductOption> productOptions
    ) {
        return GetProductInfoResult.of(
            product,
            availableLocations,
            productMoods,
            optionMap,
            productOptions
        );
    }

}
