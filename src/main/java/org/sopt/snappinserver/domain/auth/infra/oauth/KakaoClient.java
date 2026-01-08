package org.sopt.snappinserver.domain.auth.infra.oauth;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
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

    public OAuthToken fetchOAuthToken(String redirectUri, String accessCode) {
        return webClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                .with("client_id", client)
                .with("redirect_uri", redirectUri)
                .with("code", accessCode))
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .map(body -> new AuthException(AuthErrorCode.INVALID_OAUTH_TOKEN))
            )
            .bodyToMono(OAuthToken.class)
            .timeout(Duration.ofSeconds(3))
            .block();
    }

    public KakaoUserInfo fetchUserInfo(String accessToken) {
        return webClient.get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .map(body -> new AuthException(AuthErrorCode.INVALID_OAUTH_TOKEN))
            )
            .bodyToMono(KakaoUserInfo.class)
            .timeout(Duration.ofSeconds(3))
            .block();
    }
}
