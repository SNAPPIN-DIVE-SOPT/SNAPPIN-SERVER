package org.sopt.snappinserver.domain.wish.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import org.sopt.snappinserver.domain.product.repository.ProductMoodRepository;
import org.sopt.snappinserver.domain.product.repository.ProductPhotoRepository;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.repository.WishProductRepository;

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
        // @Value로 주입되는 cloudFrontDomain은 단위 테스트에서 null일 수 있어 테스트용 값 주입
        ReflectionTestUtils.setField(service, "cloudFrontDomain", "https://cdn.example.com");
    }

    @Nested
    @DisplayName("getWishedProducts")
    class GetWishedProducts {

    }
}
