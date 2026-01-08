package org.sopt.snappinserver.domain.auth.service;

import static org.sopt.snappinserver.domain.auth.domain.enums.SocialProvider.KAKAO;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.infra.oauth.KakaoClient;
import org.sopt.snappinserver.domain.auth.infra.oauth.dto.response.KakaoUserInfo;
import org.sopt.snappinserver.domain.auth.infra.oauth.dto.response.KakaoUserProfile;
import org.sopt.snappinserver.domain.auth.infra.oauth.dto.response.OAuthToken;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;
import org.sopt.snappinserver.domain.auth.service.usecase.LoginUseCase;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService implements LoginUseCase {

    private final KakaoClient kakaoClient;
    private final UserProcessor userProcessor;
    private final AuthTokenManager authTokenManager;

    @Override
    public LoginResult kakaoLogin(String accessCode, String userAgent) {
        KakaoUserProfile kakaoUserInfo = fetchKakaoUserInfo(accessCode);

        User user = userProcessor.registerOrGetUser(
            KAKAO,
            kakaoUserInfo.socialId(),
            kakaoUserInfo.nickname(),
            kakaoUserInfo.profileImage()
        );

        return authTokenManager.issueTokens(user, userAgent);
    }

    private KakaoUserProfile fetchKakaoUserInfo(String accessCode) {
        OAuthToken oAuthToken = kakaoClient.fetchOAuthToken(accessCode);

        return KakaoUserProfile.from(kakaoClient.fetchUserInfo(oAuthToken.accessToken()));
    }
}
