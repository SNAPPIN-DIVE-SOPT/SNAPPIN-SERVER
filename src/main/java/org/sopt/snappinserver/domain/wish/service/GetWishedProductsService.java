package org.sopt.snappinserver.domain.wish.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductsPageResult;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductsResult;
import org.sopt.snappinserver.domain.wish.service.usecase.GetWishedProductsUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetWishedProductsService implements GetWishedProductsUseCase {

    private static final int PAGE_SIZE = 10;
    private static final long MIN_CURSOR_VALUE = 1L;

    private final WishProductRepository wishProductRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final ProductMoodRepository productMoodRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public WishedProductsResult getWishedProducts(Long userId) {
        User user = getUser(userId);
        List<WishProduct> wishes =
            wishProductRepository.findAllByUserWithProductOrderByCreatedAtDesc(user);
        List<WishedProductResult> results = mapWishesToResults(wishes);

        return WishedProductsResult.from(results);
    }

    @Override
    public WishedProductsPageResult getWishedProductsPage(Long userId, Long cursor) {
        User user = getUser(userId);
        validateCursor(cursor);

        Pageable pageable = PageRequest.of(0, PAGE_SIZE + 1);
        List<WishProduct> wishes =
            (cursor == null)
                ? wishProductRepository.findAllByUserWithProductOrderByIdDesc(user, pageable)
                : wishProductRepository.findAllByUserWithProductOrderByIdDescAndCursor(
                    user,
                    cursor,
                    pageable
                );

        boolean hasNext = wishes.size() > PAGE_SIZE;
        if (hasNext) {
            wishes = wishes.subList(0, PAGE_SIZE);
        }

        if (wishes.isEmpty()) {
            return WishedProductsPageResult.from(List.of(), null, false);
        }

        List<WishedProductResult> results = mapWishesToResults(wishes);

        Long nextCursor = hasNext ? wishes.get(wishes.size() - 1).getId() : null;

        return WishedProductsPageResult.from(results, nextCursor, hasNext);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new WishException(WishErrorCode.USER_NOT_FOUND));
    }

    private List<WishedProductResult> mapWishesToResults(List<WishProduct> wishes) {
        return wishes.stream()
            .map(WishProduct::getProduct)
            .map(this::mapToWishedProductResult)
            .toList();
    }

    private static void validateCursor(Long cursor) {
        if (cursor != null && cursor < MIN_CURSOR_VALUE) {
            throw new WishException(WishErrorCode.INVALID_CURSOR);
        }
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
            product.getPhotographer().getNickname(),
            product.getPrice(),
            moods
        );
    }

    private String findThumbnailImageUrl(Product product) {
        return productPhotoRepository
            .findFirstByProductOrderByDisplayOrderAsc(product)
            .map(ProductPhoto::getPhoto)
            .map(photo -> cloudFrontDomain + photo.getImageUrl())
            .orElse(null);
    }

    private List<String> findMoodNames(Product product) {
        return productMoodRepository
            .findAllByProductOrderById(product)
            .stream()
            .map(productMood -> productMood.getMood().getName())
            .toList();
    }
}

