package org.sopt.snappinserver.domain.wish.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.snappinserver.domain.portfolio.repository.PortfolioPhotoRepository;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.repository.WishPortfolioRepository;

@ExtendWith(MockitoExtension.class)
class GetWishedPortfoliosServiceTest {

    @Mock private WishPortfolioRepository wishPortfolioRepository;
    @Mock private PortfolioPhotoRepository portfolioPhotoRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private GetWishedPortfoliosService service;

    @Nested
    @DisplayName("getWishedPortfolios")
    class GetWishedPortfolios {

        @Test
        @DisplayName("좋아요한 포트폴리오 목록 조회 성공 테스트")
        void getWishedPortfolios_Success() {

        }
    }
}
