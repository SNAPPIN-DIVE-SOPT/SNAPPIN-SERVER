package org.sopt.snappinserver.domain.wish.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import org.sopt.snappinserver.domain.wish.domain.exception.WishErrorCode;
import org.sopt.snappinserver.domain.wish.domain.exception.WishException;
import org.springframework.test.util.ReflectionTestUtils;

import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioPhotoRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.domain.entity.WishPortfolio;
import org.sopt.snappinserver.domain.wish.repository.WishPortfolioRepository;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosResult;

@ExtendWith(MockitoExtension.class)
class GetWishedPortfoliosServiceTest {

    private static final Long USER_ID = 1L;
    private static final Long PORTFOLIO_ID_1 = 10L;
    private static final Long PORTFOLIO_ID_2 = 20L;

    private static final String IMAGE_PATH = "/images/a.jpg";
    private static final String CDN_DOMAIN = "https://cdn.example.com";

    @Mock private WishPortfolioRepository wishPortfolioRepository;
    @Mock private PortfolioPhotoRepository portfolioPhotoRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private GetWishedPortfoliosService service;

    @BeforeEach
    void setUp() {
        // [Given] 테스트 시 필요한 환경 세팅
        // 1. @Value로 주입되는 cloudFrontDomain은 테스트에서 스프링 컨텍스트를 띄우지 않기 때문에 값이 비어있을 수 있음
        // 2. ReflectionTestUtils로 service 내부 필드(cloudFrontDomain)에 테스트용 값을 주입
        ReflectionTestUtils.setField(service, "cloudFrontDomain", CDN_DOMAIN);
    }

    @Nested
    @DisplayName("getWishedPortfolios")
    class GetWishedPortfolios {

        @Test
        @DisplayName("성공 케이스 - 대표 이미지가 있으면 cloudFrontDomain과 합쳐서 내려준다")
        void getWishedPortfolios_Success_withImage() {
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = USER_ID;

            // 2. Mock 객체 준비
            User user = mock(User.class);

            // 3. 포트폴리오 Mock 객체 준비
            Portfolio portfolio1 = mock(Portfolio.class);
            // 3-1. 포트폴리오 식별자(id) 반환값 설정
            when(portfolio1.getId()).thenReturn(PORTFOLIO_ID_1);

            // 4. 위시 엔티티 Mock 객체 준비
            WishPortfolio wish1 = mock(WishPortfolio.class);
            // 4-1. wish에서 portfolio를 꺼내면 위에서 만든 portfolio1이 나오도록 설정
            when(wish1.getPortfolio()).thenReturn(portfolio1);

            // 5. 대표 이미지 조회에 필요한 PortfolioPhoto/Photo Mock 객체 준비
            PortfolioPhoto portfolioPhoto = mock(PortfolioPhoto.class);
            Photo photo = mock(Photo.class);
            // 5-1. Photo의 imageUrl이 "/images/a.jpg"로 반환되도록 설정
            when(photo.getImageUrl()).thenReturn(IMAGE_PATH);
            // 5-2. PortfolioPhoto에서 photo를 꺼내면 위에서 만든 photo가 나오도록 설정
            when(portfolioPhoto.getPhoto()).thenReturn(photo);

            // 6. Mockito에게 행동 지시
            // 6-1. 유저 조회 성공
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // 6-2. 위시 목록 조회 결과로 wish1 하나를 반환 (최근 좋아요 순 정렬은 repo가 보장한다고 가정)
            when(wishPortfolioRepository.findAllByUserOrderByCreatedAtDesc(user)).thenReturn(List.of(wish1));
            // 6-3. 대표 이미지 조회 성공
            when(portfolioPhotoRepository.findFirstByPortfolioOrderByDisplayOrderAsc(portfolio1))
                .thenReturn(Optional.of(portfolioPhoto));

            // [When] 테스트할 서비스 메서드 호출
            WishedPortfoliosResult result = service.getWishedPortfolios(userId);

            // [Then] 결과 검증
            // 1. 결과 객체가 null이 아닌지 확인
            assertThat(result).isNotNull();
            // 2. 포트폴리오 리스트가 1개인지 확인
            assertThat(result.portfolios()).hasSize(1);
            // 3. 내려준 포트폴리오 id가 기대값(10L)인지 확인
            assertThat(result.portfolios().get(0).id()).isEqualTo(PORTFOLIO_ID_1);
            // 4. imageUrl이 cloudFrontDomain + photo.imageUrl 형태로 합쳐졌는지 확인
            assertThat(result.portfolios().get(0).imageUrl()).isEqualTo(CDN_DOMAIN + IMAGE_PATH);
        }

        @Test
        @DisplayName("성공 케이스 - 대표 이미지가 없으면 imageUrl은 null이며, repository의 정렬 순서가 유지된다")
        void getWishedPortfolios_Success_withoutImage_andKeepsOrder() {
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = USER_ID;

            // 2. 유저 Mock 객체 준비
            User user = mock(User.class);

            // 3. 포트폴리오 Mock 객체 2개 준비
            Portfolio portfolio1 = mock(Portfolio.class);
            when(portfolio1.getId()).thenReturn(PORTFOLIO_ID_1);
            Portfolio portfolio2 = mock(Portfolio.class);
            when(portfolio2.getId()).thenReturn(PORTFOLIO_ID_2);

            // 4. 위시 엔티티 Mock 객체 2개 준비
            WishPortfolio wish1 = mock(WishPortfolio.class);
            when(wish1.getPortfolio()).thenReturn(portfolio1);
            WishPortfolio wish2 = mock(WishPortfolio.class);
            when(wish2.getPortfolio()).thenReturn(portfolio2);

            // 5. Mockito에게 행동 지시
            // 5-1. 유저 조회 성공
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // 5-2. 위시 목록은 repo가 createdAt desc로 정렬해서 내려준다고 가정
            //      테스트에서는 wish2(최근) -> wish1(과거) 순으로 내려주고, 서비스가 이 순서를 유지하는지 검증
            when(wishPortfolioRepository.findAllByUserOrderByCreatedAtDesc(user)).thenReturn(List.of(wish2, wish1));

            // 5-3. 두 포트폴리오 모두 대표 이미지가 없다고 가정 (Optional.empty())
            when(portfolioPhotoRepository.findFirstByPortfolioOrderByDisplayOrderAsc(portfolio2))
                .thenReturn(Optional.empty());
            when(portfolioPhotoRepository.findFirstByPortfolioOrderByDisplayOrderAsc(portfolio1))
                .thenReturn(Optional.empty());

            // [When] 테스트할 서비스 메서드 호출
            WishedPortfoliosResult result = service.getWishedPortfolios(userId);

            // [Then] 결과 검증
            // 1. 결과 객체가 null이 아닌지 확인
            assertThat(result).isNotNull();
            // 2. 포트폴리오 리스트가 2개인지 확인 (정렬 검증을 위해 2개 이상 필요)
            assertThat(result.portfolios()).hasSize(2);

            // 3. 서비스가 중간에서 순서를 바꾸지 않고, repo에서 내려준 순서를 유지하는지 확인
            assertThat(result.portfolios().get(0).id()).isEqualTo(PORTFOLIO_ID_2);
            assertThat(result.portfolios().get(1).id()).isEqualTo(PORTFOLIO_ID_1);

            // 4. 대표 이미지가 없으면 imageUrl이 null로 내려오는지 확인
            assertThat(result.portfolios().get(0).imageUrl()).isNull();
            assertThat(result.portfolios().get(1).imageUrl()).isNull();
        }

        @Test
        @DisplayName("성공 케이스 - 위시한 포트폴리오가 없으면 빈 리스트를 반환한다")
        void getWishedPortfolios_Success_emptyWishList() {
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = USER_ID;

            // 2. 유저 Mock 객체 준비
            User user = mock(User.class);

            // 3. Mockito에게 행동 지시
            // 3-1. 유저 조회 성공
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // 3-2. 위시 포트폴리오 목록이 비어있는 경우
            when(wishPortfolioRepository.findAllByUserOrderByCreatedAtDesc(user))
                .thenReturn(List.of());

            // [When] 테스트할 서비스 메서드 호출
            WishedPortfoliosResult result = service.getWishedPortfolios(userId);

            // [Then] 결과 검증
            // 1. 결과 객체가 null이 아닌지 확인
            assertThat(result).isNotNull();
            // 2. 포트폴리오 리스트가 null이 아닌지 확인
            assertThat(result.portfolios()).isNotNull();
            // 3. 포트폴리오 리스트가 비어있는지 확인
            assertThat(result.portfolios()).isEmpty();

            // 4. 위시 목록이 없으면 대표 이미지 조회가 발생하지 않는지 확인
            verify(portfolioPhotoRepository, never())
                .findFirstByPortfolioOrderByDisplayOrderAsc(any());
        }

        @Test
        @DisplayName("예외 케이스 - 유저가 없으면 USER_NOT_FOUND 예외를 던진다")
        void throw_whenUserNotFound() {
            // [Given] 테스트 시 필요한 데이터 생성
            // 1. 요청 파라미터 준비
            Long userId = USER_ID;

            // 2. Mockito에게 행동 지시
            // 2-1. 유저 조회 실패(존재하지 않음)
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // [When/Then] 테스트할 서비스 메서드 호출 및 결과 검증
            // 1. 예외가 발생하는지 확인
            WishException ex = catchThrowableOfType(
                () -> service.getWishedPortfolios(userId), WishException.class
            );

            // 2. 예외 객체가 null이 아닌지 확인
            assertThat(ex).isNotNull();
            // 3. 에러 코드가 USER_NOT_FOUND인지 확인
            assertThat(ex.getErrorCode()).isEqualTo(WishErrorCode.USER_NOT_FOUND);
            // 4. 유저가 없으면 이후 로직(위시 조회/대표 이미지 조회)이 수행되지 않았는지 확인
            verify(wishPortfolioRepository, never()).findAllByUserOrderByCreatedAtDesc(any());
            verify(portfolioPhotoRepository, never()).findFirstByPortfolioOrderByDisplayOrderAsc(any());
        }
    }
}
