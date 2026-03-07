package org.sopt.snappinserver.domain.wish.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.springframework.test.util.ReflectionTestUtils;

import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.entity.ProductMood;
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
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductsResult;

@ExtendWith(MockitoExtension.class)
class GetWishedProductsServiceTest {

    @Mock private WishProductRepository wishProductRepository;
    @Mock private ProductPhotoRepository productPhotoRepository;
    @Mock private ProductMoodRepository productMoodRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private GetWishedProductsService service;

    @BeforeEach
    void setUp() {
        // [Given] 테스트 시 필요한 환경 세팅
        // 1. @Value로 주입되는 cloudFrontDomain은 단위 테스트에서 null일 수 있음
        // 2. ReflectionTestUtils로 service 내부 필드(cloudFrontDomain)에 테스트용 값을 주입
        ReflectionTestUtils.setField(service, "cloudFrontDomain", "https://cdn.example.com");
    }

    @Nested
    @DisplayName("getWishedProducts")
    class GetWishedProducts {
        @Test
        @DisplayName("성공 케이스 - 대표 이미지가 있으면 cloudFrontDomain과 합쳐서 내려주며, 리뷰/무드 정보도 함께 내려준다")
        void getWishedProducts_Success_withImage_review_moods() {
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = 1L;

            // 2. 유저 Mock 객체 준비
            User user = mock(User.class);

            // 3. 상품(Product) Mock 객체 준비
            Product product = mock(Product.class);
            when(product.getId()).thenReturn(10L);
            when(product.getTitle()).thenReturn("스냅 촬영 상품");
            when(product.getPrice()).thenReturn(100000);

            // 3-1. 작가(photographer) 닉네임 객체 준비
            var photographer = mock(Photographer.class);
            when(photographer.getNickname()).thenReturn("작가");
            when(product.getPhotographer()).thenReturn(photographer);

            // 4. 위시 엔티티(WishProduct) Mock 객체 준비
            WishProduct wish = mock(WishProduct.class);
            when(wish.getProduct()).thenReturn(product);

            // 5. 대표 이미지 조회에 필요한 ProductPhoto/Photo Mock 객체 준비
            ProductPhoto productPhoto = mock(ProductPhoto.class);
            var photo = mock(Photo.class);
            when(photo.getImageUrl()).thenReturn("/images/a.jpg");
            when(productPhoto.getPhoto()).thenReturn(photo);

            // 6. 리뷰 통계 Mock 객체 준비 (평균 별점/리뷰 수)
            ProductReviewStatsResult stats = mock(ProductReviewStatsResult.class);
            when(stats.averageRating()).thenReturn(4.5);
            when(stats.reviewCount()).thenReturn(10L);

            // 7. 무드 태그(ProductMood -> Mood -> name) Mock 객체 준비
            ProductMood productMood = mock(ProductMood.class);
            var mood = mock(Mood.class);
            when(mood.getName()).thenReturn("따뜻한");
            when(productMood.getMood()).thenReturn(mood);

            // 8. Mockito에게 행동 지시
            // 8-1. 유저 조회 성공
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // 8-2. 위시 목록 조회 결과로 wish 하나 반환 (정렬은 repo가 보장한다고 가정)
            when(wishProductRepository.findAllByUserWithProductOrderByCreatedAtDesc(user))
                .thenReturn(List.of(wish));
            // 8-3. 대표 이미지 조회 성공
            when(productPhotoRepository.findFirstByProductOrderByDisplayOrderAsc(product))
                .thenReturn(Optional.of(productPhoto));
            // 8-4. 리뷰 통계 조회 성공
            when(reviewRepository.findReviewStatsByProductId(10L)).thenReturn(stats);
            // 8-5. 무드 태그 조회 성공
            when(productMoodRepository.findAllByProductOrderById(product))
                .thenReturn(List.of(productMood));

            // [When] 테스트할 서비스 메서드 호출
            WishedProductsResult result = service.getWishedProducts(userId);

            // [Then] 결과 검증
            // 1. 결과 객체가 null이 아닌지 확인
            assertThat(result).isNotNull();
            // 2. 상품 리스트가 1개인지 확인
            assertThat(result.products()).hasSize(1);

            var item = result.products().get(0);

            // 3. id, title, price 매핑 확인
            assertThat(item.id()).isEqualTo(10L);
            assertThat(item.title()).isEqualTo("스냅 촬영 상품");
            assertThat(item.price()).isEqualTo(100000);

            // 4. 대표 이미지가 cloudFrontDomain + imageUrl 형태로 합쳐졌는지 확인
            assertThat(item.imageUrl()).isEqualTo("https://cdn.example.com/images/a.jpg");

            // 5. 리뷰 통계가 매핑되는지 확인
            assertThat(item.rate()).isEqualTo(4.5);
            assertThat(item.reviewCount()).isEqualTo(10);

            // 6. 작가명 매핑 확인
            assertThat(item.photographer()).isEqualTo("작가");

            // 7. 무드 태그 매핑 확인
            assertThat(item.moods()).containsExactly("따뜻한");
        }

        @Test
        @DisplayName("성공 케이스 - 대표 이미지가 없으면 imageUrl은 null이다")
        void getWishedProducts_Success_withoutImage() {
            // [Given] 테스트 시 필요한 데이터 생성
            Long userId = 1L;

            User user = mock(User.class);

            Product product = mock(Product.class);
            when(product.getId()).thenReturn(10L);
            when(product.getTitle()).thenReturn("상품");
            when(product.getPrice()).thenReturn(50000);

            var photographer = mock(Photographer.class);
            when(photographer.getNickname()).thenReturn("작가");
            when(product.getPhotographer()).thenReturn(photographer);

            WishProduct wish = mock(WishProduct.class);
            when(wish.getProduct()).thenReturn(product);

            ProductReviewStatsResult stats = mock(ProductReviewStatsResult.class);
            when(stats.averageRating()).thenReturn(4.0);
            when(stats.reviewCount()).thenReturn(2L);

            // Mockito에게 행동 지시
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(wishProductRepository.findAllByUserWithProductOrderByCreatedAtDesc(user))
                .thenReturn(List.of(wish));

            // 대표 이미지 없음
            when(productPhotoRepository.findFirstByProductOrderByDisplayOrderAsc(product))
                .thenReturn(Optional.empty());

            when(reviewRepository.findReviewStatsByProductId(10L)).thenReturn(stats);
            when(productMoodRepository.findAllByProductOrderById(product)).thenReturn(List.of());

            // [When]
            WishedProductsResult result = service.getWishedProducts(userId);

            // [Then]
            assertThat(result).isNotNull();
            assertThat(result.products()).hasSize(1);
            assertThat(result.products().get(0).imageUrl()).isNull();
        }

        @Test
        @DisplayName("성공 케이스 - repository의 최신 좋아요 순(createdAt desc) 정렬을 서비스가 그대로 유지한다")
        void getWishedProducts_Success_keepsOrder() {
            // [Given] 테스트 시 필요한 데이터 생성
            Long userId = 1L;

            User user = mock(User.class);

            Product product1 = mock(Product.class);
            when(product1.getId()).thenReturn(10L);
            var photographer1 = mock(Photographer.class);
            when(photographer1.getNickname()).thenReturn("작가1");
            when(product1.getPhotographer()).thenReturn(photographer1);
            when(product1.getTitle()).thenReturn("상품1");
            when(product1.getPrice()).thenReturn(1000);

            Product product2 = mock(Product.class);
            when(product2.getId()).thenReturn(20L);
            var photographer2 = mock(Photographer.class);
            when(photographer2.getNickname()).thenReturn("작가2");
            when(product2.getPhotographer()).thenReturn(photographer2);
            when(product2.getTitle()).thenReturn("상품2");
            when(product2.getPrice()).thenReturn(2000);

            WishProduct wish1 = mock(WishProduct.class);
            when(wish1.getProduct()).thenReturn(product1);
            WishProduct wish2 = mock(WishProduct.class);
            when(wish2.getProduct()).thenReturn(product2);

            // 리뷰 통계는 각 상품마다 호출되므로, 공통 stub 처리
            ProductReviewStatsResult stats = mock(ProductReviewStatsResult.class);
            when(stats.averageRating()).thenReturn(0.0);
            when(stats.reviewCount()).thenReturn(0L);

            // [Given - Mockito 행동 지시]
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // repo가 최신순으로 내려준다고 가정: wish2(최근) -> wish1(과거)
            when(wishProductRepository.findAllByUserWithProductOrderByCreatedAtDesc(user))
                .thenReturn(List.of(wish2, wish1));

            when(productPhotoRepository.findFirstByProductOrderByDisplayOrderAsc(any()))
                .thenReturn(Optional.empty());

            when(reviewRepository.findReviewStatsByProductId(any()))
                .thenReturn(stats);

            when(productMoodRepository.findAllByProductOrderById(any()))
                .thenReturn(List.of());

            // [When]
            WishedProductsResult result = service.getWishedProducts(userId);

            // [Then]
            assertThat(result).isNotNull();
            assertThat(result.products()).hasSize(2);

            // 서비스가 중간에서 순서를 바꾸지 않는지 확인
            assertThat(result.products().get(0).id()).isEqualTo(20L);
            assertThat(result.products().get(1).id()).isEqualTo(10L);

        }
    }
}
