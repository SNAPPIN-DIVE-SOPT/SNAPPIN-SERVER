package org.sopt.snappinserver.domain.wish.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.domain.entity.WishProduct;
import org.sopt.snappinserver.domain.wish.domain.exception.WishErrorCode;
import org.sopt.snappinserver.domain.wish.domain.exception.WishException;
import org.sopt.snappinserver.domain.wish.repository.WishProductRepository;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishProductResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostWishProductServiceTest {

    @Mock private WishProductRepository wishProductRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks private PostWishProductService postWishProductService;

    @Nested
    @DisplayName("toggleProductWish")
    class ToggleProductWish {

        @Test
        @DisplayName("기존 위시가 없으면 위시를 생성하고 liked=true를 반환한다")
        void like_whenNotExists() {
            Long userId = 1L;
            Long productId = 10L;

            User user = mock(User.class);
            Product product = mock(Product.class);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(wishProductRepository.findByUserAndProduct(user, product)).thenReturn(Optional.empty());

            WishProductResult result = postWishProductService.toggleProductWish(userId, productId);

            assertThat(result).isNotNull();
            assertThat(result.productId()).isEqualTo(productId);
            assertThat(result.liked()).isTrue();
            verify(wishProductRepository, times(1)).save(any(WishProduct.class));
            verify(wishProductRepository, never()).delete(any(WishProduct.class));
        }

        @Test
        @DisplayName("기존 위시가 있으면 위시를 삭제하고 liked=false를 반환한다")
        void cancel_whenExists() {
            Long userId = 1L;
            Long productId = 10L;

            User user = mock(User.class);
            Product product = mock(Product.class);
            WishProduct existingWish = mock(WishProduct.class);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(wishProductRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(existingWish));

            WishProductResult result = postWishProductService.toggleProductWish(userId, productId);

            assertThat(result).isNotNull();
            assertThat(result.productId()).isEqualTo(productId);
            assertThat(result.liked()).isFalse();
            verify(wishProductRepository, times(1)).delete(existingWish);
            verify(wishProductRepository, never()).save(any(WishProduct.class));
        }

        @Test
        @DisplayName("유저가 없으면 USER_NOT_FOUND 예외를 던진다")
        void throw_whenUserNotFound() {
            Long userId = 1L;
            Long productId = 10L;

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            WishException ex = catchThrowableOfType(
                () -> postWishProductService.toggleProductWish(userId, productId),
                WishException.class
            );

            assertThat(ex).isNotNull();
            assertThat(ex.getErrorCode()).isEqualTo(WishErrorCode.USER_NOT_FOUND);

            verify(productRepository, never()).findById(anyLong());
            verify(wishProductRepository, never()).findByUserAndProduct(any(), any());
            verify(wishProductRepository, never()).save(any());
            verify(wishProductRepository, never()).delete(any());
        }

        @Test
        @DisplayName("존재하지 않는 상품일 경우 PRODUCT_NOT_FOUND 예외를 던진다")
        void throw_whenProductNotFound() {
            Long userId = 1L;
            Long productId = 10L;

            User user = mock(User.class);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            WishException ex = catchThrowableOfType(
                () -> postWishProductService.toggleProductWish(userId, productId),
                WishException.class
            );

            assertThat(ex).isNotNull();
            assertThat(ex.getErrorCode()).isEqualTo(WishErrorCode.PRODUCT_NOT_FOUND);

            verify(wishProductRepository, never()).findByUserAndProduct(any(), any());
            verify(wishProductRepository, never()).save(any());
            verify(wishProductRepository, never()).delete(any());
        }
    }
}
