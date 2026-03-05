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
import org.mockito.ArgumentCaptor;

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
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = 1L;
            Long productId = 10L;

            // 2. 유저/상품 Mock 객체 준비
            User user = mock(User.class);
            Product product = mock(Product.class);

            // 3. Mockito에게 행동 지시
            // 3-1. 유저 조회 성공
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // 3-2. 상품 조회 성공
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            // 3-3. 기존 위시가 없다고 가정
            when(wishProductRepository.findByUserAndProduct(user, product)).thenReturn(Optional.empty());

            // [When] 테스트할 서비스 메서드 호출
            WishProductResult result = postWishProductService.toggleProductWish(userId, productId);

            // [Then] 결과 검증
            // 1. 결과 객체가 null이 아닌지 확인
            assertThat(result).isNotNull();
            // 2. 결과 productId가 요청 productId와 동일한지 확인
            assertThat(result.productId()).isEqualTo(productId);
            // 3. 위시 생성이므로 liked=true인지 확인
            assertThat(result.liked()).isTrue();
            // 4. 저장이 1번 호출되었는지 확인
            ArgumentCaptor<WishProduct> captor = ArgumentCaptor.forClass(WishProduct.class);
            verify(wishProductRepository, times(1)).save(captor.capture());
            // 5. 저장된 위시가 요청 user/product로 생성되었는지 확인
            WishProduct savedWish = captor.getValue();
            assertThat(savedWish.getUser()).isSameAs(user);
            assertThat(savedWish.getProduct()).isSameAs(product);
            // 6. 삭제는 호출되지 않았는지 확인
            verify(wishProductRepository, never()).delete(any(WishProduct.class));
        }

        @Test
        @DisplayName("기존 위시가 있으면 위시를 삭제하고 liked=false를 반환한다")
        void cancel_whenExists() {
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = 1L;
            Long productId = 10L;

            // 2. 유저/상품/기존 위시 Mock 객체 준비
            User user = mock(User.class);
            Product product = mock(Product.class);
            WishProduct existingWish = mock(WishProduct.class);

            // 3. Mockito에게 행동 지시
            // 3-1. 유저 조회 성공
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // 3-2. 상품 조회 성공
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            // 3-3. 기존 위시가 존재한다고 가정
            when(wishProductRepository.findByUserAndProduct(user, product)).thenReturn(Optional.of(existingWish));

            // [When] 테스트할 서비스 메서드 호출
            WishProductResult result = postWishProductService.toggleProductWish(userId, productId);

            // [Then] 결과 검증
            // 1. 결과 객체가 null이 아닌지 확인
            assertThat(result).isNotNull();
            // 2. 결과 productId가 요청 productId와 동일한지 확인
            assertThat(result.productId()).isEqualTo(productId);
            // 3. 위시 취소이므로 liked=false인지 확인
            assertThat(result.liked()).isFalse();
            // 4. 삭제가 1번 호출되었는지 확인
            verify(wishProductRepository, times(1)).delete(existingWish);
            // 5. 저장은 호출되지 않았는지 확인
            verify(wishProductRepository, never()).save(any(WishProduct.class));
        }

        @Test
        @DisplayName("유저가 없으면 USER_NOT_FOUND 예외를 던진다")
        void throw_whenUserNotFound() {
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = 1L;
            Long productId = 10L;

            // 2. Mockito에게 행동 지시
            // 2-1. 유저 조회 실패(존재하지 않음)
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // [When] 테스트할 서비스 메서드 호출

            // [Then] 결과 검증
            // 1. 예외가 발생하는지 확인
            WishException ex = catchThrowableOfType(
                () -> postWishProductService.toggleProductWish(userId, productId),
                WishException.class
            );

            // 2. 예외 객체가 null이 아닌지 확인
            assertThat(ex).isNotNull();

            // 3. 에러 코드가 USER_NOT_FOUND인지 확인
            assertThat(ex.getErrorCode()).isEqualTo(WishErrorCode.USER_NOT_FOUND);

            // 4. 유저가 없으면 이후 로직이 수행되지 않았는지 확인
            verify(productRepository, never()).findById(anyLong());
            verify(wishProductRepository, never()).findByUserAndProduct(any(), any());
            verify(wishProductRepository, never()).save(any());
            verify(wishProductRepository, never()).delete(any());
        }

        @Test
        @DisplayName("상품이 없으면 PRODUCT_NOT_FOUND 예외를 던진다")
        void throw_whenProductNotFound() {
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = 1L;
            Long productId = 10L;

            // 2. 유저 Mock 객체 준비
            User user = mock(User.class);

            // 3. Mockito에게 행동 지시
            // 3-1. 유저 조회 성공
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // 3-2. 상품 조회 실패(존재하지 않음)
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // [When/Then] 테스트할 서비스 메서드 호출 및 결과 검증
            // 1. 예외가 발생하는지 확인
            WishException ex = catchThrowableOfType(
                () -> postWishProductService.toggleProductWish(userId, productId),
                WishException.class
            );

            // 2. 예외 객체가 null이 아닌지 확인
            assertThat(ex).isNotNull();

            // 3. 에러 코드가 PRODUCT_NOT_FOUND인지 확인
            assertThat(ex.getErrorCode()).isEqualTo(WishErrorCode.PRODUCT_NOT_FOUND);

            // 4. 상품이 없으면 위시 조회/저장/삭제가 수행되지 않았는지 확인
            verify(wishProductRepository, never()).findByUserAndProduct(any(), any());
            verify(wishProductRepository, never()).save(any());
            verify(wishProductRepository, never()).delete(any());
        }
    }
}
