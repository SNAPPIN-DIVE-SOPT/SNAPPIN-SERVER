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

        }
        
    }
}
