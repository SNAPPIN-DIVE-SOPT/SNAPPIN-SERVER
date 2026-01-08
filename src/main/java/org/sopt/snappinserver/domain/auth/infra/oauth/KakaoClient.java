package org.sopt.snappinserver.domain.auth.infra.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.snappinserver.domain.auth.infra.oauth.dto.response.KakaoUserInfo;
import org.sopt.snappinserver.domain.auth.infra.oauth.dto.response.OAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
@Slf4j
public class KakaoClient {

    private final WebClient webClient;

    @Value("${kakao.auth.client}")
    private String client;

    @Value("${kakao.auth.redirect}")
    private String redirect;

    public OAuthToken fetchOAuthToken(String accessCode) {
        return webClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                .with("client_id", client)
                .with("redirect_uri", redirect)
                .with("code", accessCode))
            .retrieve()
            .bodyToMono(OAuthToken.class)
            .block();
    }

    public KakaoUserInfo fetchUserInfo(String accessToken) {
        return webClient.get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(KakaoUserInfo.class)
            .block();
    }
}
