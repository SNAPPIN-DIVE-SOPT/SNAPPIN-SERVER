package org.sopt.snappinserver.api.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.snappinserver.api.auth.code.AuthSuccessCode;
import org.sopt.snappinserver.api.auth.dto.request.CreateKakaoLoginRequest;
import org.sopt.snappinserver.api.auth.dto.response.CreateKakaoLoginResponse;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;
import org.sopt.snappinserver.domain.auth.service.usecase.LoginUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController implements AuthApi {

    private final LoginUseCase loginUseCase;

    @Value("${auth.cookie.secure}")
    private boolean isSecure;

    @Value("${jwt.refresh-token-ttl-seconds}")
    private long refreshTokenSeconds;

    @Override
    @PostMapping("/login/kakao")
    public ApiResponseBody<CreateKakaoLoginResponse, Void> createKakaoLogin(
        @RequestParam(name = "redirect_uri", required = false) String clientRedirectUri,
        @Valid @RequestBody CreateKakaoLoginRequest createKakaoLoginRequest,
        @RequestHeader(value = "User-Agent", required = false) String userAgent,
        HttpServletResponse httpServletResponse
    ) {
        String redirectUri =
            (clientRedirectUri == null)
                ? ("http://localhost:8080/api/v1/auth/login/kakao")
                : clientRedirectUri;
        log.info("redirectUri: {}", redirectUri);
        LoginResult loginResult = loginUseCase.kakaoLogin(
            redirectUri,
            createKakaoLoginRequest.code(),
            userAgent
        );
        ResponseCookie refreshCookie = getResponseCookie(loginResult);
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponseBody.ok(
            AuthSuccessCode.LOGIN_SUCCESS,
            new CreateKakaoLoginResponse(loginResult.accessToken())
        );
    }

    private ResponseCookie getResponseCookie(LoginResult loginResult) {
        return ResponseCookie.from("refreshToken", loginResult.refreshToken())
            .httpOnly(true)
            .secure(isSecure)
            .sameSite(isSecure ? "None" : "Lax")
            .path("/")
            .maxAge(refreshTokenSeconds)
            .build();
    }

}
