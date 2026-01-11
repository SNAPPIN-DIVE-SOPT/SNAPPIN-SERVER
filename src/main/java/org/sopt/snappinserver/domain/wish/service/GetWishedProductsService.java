package org.sopt.snappinserver.domain.wish.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductPhoto;
import org.sopt.snappinserver.domain.product.repository.ProductMoodRepository;
import org.sopt.snappinserver.domain.product.repository.ProductPhotoRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.domain.entity.WishProduct;
import org.sopt.snappinserver.domain.wish.domain.exception.WishErrorCode;
import org.sopt.snappinserver.domain.wish.domain.exception.WishException;
import org.sopt.snappinserver.domain.wish.repository.WishProductRepository;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductResult;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductsResult;
import org.sopt.snappinserver.domain.wish.service.usecase.GetWishedProductsUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetWishedProductsService implements GetWishedProductsUseCase {

    private final WishProductRepository wishProductRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final ProductMoodRepository productMoodRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public WishedProductsResult getWishedProducts(Long userId) {
        User user = getUser(userId);
        List<WishedProductResult> results = getWishedProductResults(user);

        return WishedProductsResult.from(results);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new WishException(WishErrorCode.USER_NOT_FOUND));
    }

    private List<WishedProductResult> getWishedProductResults(User user) {
        return wishProductRepository
            .findAllByUser(user)
            .stream()
            .map(WishProduct::getProduct)
            .map(this::mapToWishedProductResult)
            .toList();
    }

    private WishedProductResult mapToWishedProductResult(Product product) {
        String imageUrl = findThumbnailImageUrl(product);
        ProductReviewStatsResult reviewStats = reviewRepository.findReviewStatsByProductId(
            product.getId()
        );

        List<String> moods = findMoodNames(product);

        return WishedProductResult.of(
            product.getId(),
            imageUrl,
            product.getTitle(),
            reviewStats.averageRating(),
            (int) reviewStats.reviewCount(),
            product.getPhotographer().getName(),
            product.getPrice(),
            moods
        );
    }

    private String findThumbnailImageUrl(Product product) {
        return productPhotoRepository
            .findFirstByProductOrderByDisplayOrderAsc(product)
            .map(ProductPhoto::getPhoto)
            .map(Photo::getImageUrl)
            .orElse(null);
    }

    private List<String> findMoodNames(Product product) {
        return productMoodRepository
            .findAllByProduct(product)
            .stream()
            .map(productMood -> productMood.getMood().getName())
            .toList();
    }
}

