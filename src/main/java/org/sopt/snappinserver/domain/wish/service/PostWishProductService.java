package org.sopt.snappinserver.domain.wish.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepository;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.domain.entity.WishPortfolio;
import org.sopt.snappinserver.domain.wish.domain.entity.WishProduct;
import org.sopt.snappinserver.domain.wish.domain.exception.WishErrorCode;
import org.sopt.snappinserver.domain.wish.domain.exception.WishException;
import org.sopt.snappinserver.domain.wish.repository.WishPortfolioRepository;
import org.sopt.snappinserver.domain.wish.repository.WishProductRepository;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishPortfolioResult;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishProductResult;
import org.sopt.snappinserver.domain.wish.service.usecase.PostWishPortfolioUseCase;
import org.sopt.snappinserver.domain.wish.service.usecase.PostWishProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostWishProductService implements PostWishProductUseCase {

    private final WishProductRepository wishProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public WishProductResult toggleProductWish(Long userId, Long productId) {
        User user = getUser(userId);
        Product product = getProduct(productId);

        return wishProductRepository.findByUserAndProduct(user, product)
            .map(existingWish -> cancelWish(existingWish, productId))
            .orElseGet(() -> likeWish(user, product, productId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new WishException(WishErrorCode.USER_NOT_FOUND));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new WishException(WishErrorCode.PORTFOLIO_NOT_FOUND));
    }

    private WishProductResult cancelWish(WishProduct existingWish, Long productId) {
        wishProductRepository.delete(existingWish);
        return WishProductResult.of(productId, false);
    }

    private WishProductResult likeWish(User user, Product product, Long productId) {
        WishProduct newWish = WishProduct.create(user, product);
        wishProductRepository.save(newWish);
        return WishProductResult.of(productId, true);
    }

}
