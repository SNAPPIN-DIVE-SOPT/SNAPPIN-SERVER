package org.sopt.snappinserver.api.v2.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.snappinserver.api.v2.auth.dto.request.CreateKakaoLoginRequest;
import org.sopt.snappinserver.api.v2.auth.dto.response.CreateKakaoLoginResponse;
import org.sopt.snappinserver.domain.auth.facade.AuthFacade;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginWithPhotographerProfileResult;
import org.sopt.snappinserver.global.response.code.auth.AuthSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthControllerV2 implements AuthApi {

    private final AuthFacade authFacade;

    @Value("${auth.cookie.secure}")
    private boolean isSecure;

    @Value("${jwt.refresh-token-ttl-seconds}")
    private long refreshTokenSeconds;

    @Override
    public ApiResponseBody<CreateKakaoLoginResponse, Void> createKakaoLogin(
        String clientRedirectUri,
        CreateKakaoLoginRequest createKakaoLoginRequest,
        String userAgent,
        HttpServletResponse httpServletResponse
    ) {
        String redirectUri =
            (clientRedirectUri == null)
                ? ("http://localhost:8080/api/v2/auth/login/kakao")
                : clientRedirectUri;
        LoginWithPhotographerProfileResult loginResult = authFacade.loginWithPhotographerProfile(
            redirectUri,
            createKakaoLoginRequest.code(),
            userAgent
        );
        ResponseCookie refreshCookie = getResponseCookie(loginResult.refreshToken());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponseBody.ok(
            AuthSuccessCode.LOGIN_SUCCESS,
            CreateKakaoLoginResponse.from(loginResult)
        );
    }

    private ResponseCookie getResponseCookie(String refreshTokenValue) {
        return ResponseCookie.from("refreshToken", refreshTokenValue)
            .httpOnly(true)
            .secure(isSecure)
            .sameSite(isSecure ? "None" : "Lax")
            .path("/")
            .maxAge(refreshTokenSeconds)
            .build();
    }
}
