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
import org.sopt.snappinserver.domain.wish.repository.WishProductRepository;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishProductResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    }
}
