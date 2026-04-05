package org.sopt.snappinserver.domain.product.service;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.photo.repository.PhotoRepository;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.request.CreateProductReviewCommand;
import org.sopt.snappinserver.domain.product.service.dto.response.CreateProductReviewResult;
import org.sopt.snappinserver.domain.product.service.usecase.PostProductReviewUseCase;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.sopt.snappinserver.domain.review.domain.entity.ReviewPhoto;
import org.sopt.snappinserver.domain.review.repository.ProductReviewRepository;
import org.sopt.snappinserver.domain.review.repository.ReviewPhotoRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class PostProductReviewService implements PostProductReviewUseCase {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductReviewRepository productReviewRepository;
    private final PhotoRepository photoRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Override
    public CreateProductReviewResult createProductReview(CreateProductReviewCommand command) {
        User author = getUser(command.userId());
        Product product = getProduct(command.productId());

        Review review = Review.create(author, product, command.rating(), command.content());
        productReviewRepository.save(review);

        createAndSaveReviewPhotos(command, review);

        return CreateProductReviewResult.of(review.getId(), product.getId());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new UserException(UserErrorCode.USER_NOT_FOUND)
        );
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND)
        );
    }

    private void createAndSaveReviewPhotos(CreateProductReviewCommand command, Review review) {
        if (!CollectionUtils.isEmpty(command.imageUrls())) {
            List<Photo> photos = command.imageUrls().stream().map(Photo::create).toList();
            photoRepository.saveAll(photos);

            List<ReviewPhoto> reviewPhotos = IntStream
                .range(0, photos.size())
                .mapToObj(i -> ReviewPhoto.create(review, photos.get(i), i + 1))
                .toList();
            reviewPhotoRepository.saveAll(reviewPhotos);
        }
    }
}
