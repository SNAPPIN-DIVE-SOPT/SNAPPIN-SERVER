package org.sopt.snappinserver.domain.auth.service;

import static org.sopt.snappinserver.domain.auth.domain.enums.SocialProvider.KAKAO;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.infra.oauth.KakaoClient;
import org.sopt.snappinserver.domain.auth.infra.oauth.dto.response.KakaoUserInfo;
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
        KakaoUserInfo kakaoUserInfo = fetchKakaoUserInfo(accessCode);

        User user = userProcessor.registerOrGetUser(
            KAKAO,
            kakaoUserInfo.id().toString(),
            kakaoUserInfo.kakaoAccount().profile().nickname(),
            kakaoUserInfo.kakaoAccount().profile().profileImageUrl()
        );

        return authTokenManager.issueTokens(user, userAgent);
    }

    private KakaoUserInfo fetchKakaoUserInfo(String accessCode) {
        OAuthToken oAuthToken = kakaoClient.fetchOAuthToken(accessCode);

        return kakaoClient.fetchUserInfo(oAuthToken.accessToken());
    }
}
