package org.sopt.snappinserver.domain.wish.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.repository.WishProductRepository;

@ExtendWith(MockitoExtension.class)
class PostWishProductServiceTest {

    @Mock private WishProductRepository wishProductRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks private PostWishProductService postWishProductService;

    @Nested
    @DisplayName("toggleProductWish")
    class ToggleProductWish {

    }
}
