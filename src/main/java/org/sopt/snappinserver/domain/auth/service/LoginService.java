package org.sopt.snappinserver.domain.auth.service;

import static org.sopt.snappinserver.domain.auth.domain.enums.SocialProvider.KAKAO;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.auth.infra.oauth.KakaoClient;
import org.sopt.snappinserver.domain.auth.infra.oauth.dto.response.KakaoUserProfile;
import org.sopt.snappinserver.domain.auth.infra.oauth.dto.response.OAuthToken;
import org.sopt.snappinserver.domain.auth.repository.AuthProviderRepository;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;
import org.sopt.snappinserver.domain.auth.service.token.AuthTokenManager;
import org.sopt.snappinserver.domain.auth.service.usecase.LoginUseCase;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService implements LoginUseCase {

    private final KakaoClient kakaoClient;
    private final AuthProviderRepository authProviderRepository;
    private final GetSocialUserService getSocialUserService;
    private final AuthTokenManager authTokenManager;

    @Override
    public LoginResult kakaoLogin(String redirectUri, String accessCode, String userAgent) {
        KakaoUserProfile kakaoUserInfo = fetchKakaoUserInfo(redirectUri, accessCode);
        boolean isNew = !authProviderRepository
            .existsBySocialProviderAndProviderId(KAKAO, kakaoUserInfo.socialId());
        User user = getSocialUserService.registerOrGetUser(
            KAKAO,
            kakaoUserInfo.socialId(),
            kakaoUserInfo.nickname()
        );
        TokenPair tokenPair = authTokenManager.issueTokenPair(user, userAgent);

        return LoginResult.of(isNew, tokenPair, user);
    }

    private KakaoUserProfile fetchKakaoUserInfo(String redirectUri, String accessCode) {
        OAuthToken oAuthToken = kakaoClient.fetchOAuthToken(redirectUri, accessCode);

        return KakaoUserProfile.from(kakaoClient.fetchUserInfo(oAuthToken.accessToken()));
    }
}
