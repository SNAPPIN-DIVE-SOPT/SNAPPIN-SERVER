package org.sopt.snappinserver.api.v1.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.snappinserver.api.v1.auth.dto.request.CreateKakaoLoginRequest;
import org.sopt.snappinserver.api.v1.auth.dto.response.CreateAccessTokenResponse;
import org.sopt.snappinserver.api.v1.auth.dto.response.CreateKakaoLoginResponse;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;
import org.sopt.snappinserver.domain.auth.service.dto.response.ReissueTokenResult;
import org.sopt.snappinserver.domain.auth.service.usecase.LoginUseCase;
import org.sopt.snappinserver.domain.auth.service.usecase.LogoutUseCase;
import org.sopt.snappinserver.domain.auth.service.usecase.ReissueTokenUseCase;
import org.sopt.snappinserver.global.response.code.auth.AuthSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController implements AuthApi {

    private final LoginUseCase loginUseCase;
    private final ReissueTokenUseCase reissueTokenUseCase;
    private final LogoutUseCase logoutUseCase;

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
                ? ("http://localhost:8080/api/v1/auth/login/kakao")
                : clientRedirectUri;
        LoginResult loginResult = loginUseCase.kakaoLogin(
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

    @Override
    public ApiResponseBody<CreateAccessTokenResponse, Void> createReissuedTokens(
        String refreshToken,
        String userAgent,
        HttpServletResponse httpServletResponse
    ) {
        validateCookieExists(refreshToken);
        ReissueTokenResult reissueTokenResult = reissueTokenUseCase.reissueToken(
            refreshToken,
            userAgent
        );
        CreateAccessTokenResponse createAccessTokenResponse = CreateAccessTokenResponse.from(
            reissueTokenResult
        );
        ResponseCookie refreshCookie = getResponseCookie(reissueTokenResult.refreshToken());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponseBody.ok(
            AuthSuccessCode.REISSUE_TOKENS_SUCCESS,
            createAccessTokenResponse
        );
    }

    @Override
    public ApiResponseBody<Void, Void> logout(
        @AuthenticationPrincipal CustomUserInfo principal,
        String refreshToken,
        HttpServletResponse httpServletResponse
    ) {
        logoutUseCase.logout(principal.userId(), refreshToken);
        ResponseCookie deletedCookie = getDeletedCookie();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, deletedCookie.toString());

        return ApiResponseBody.ok(AuthSuccessCode.LOGOUT_SUCCESS);
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

    private void validateCookieExists(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_COOKIE_REQUIRED);
        }
    }

    private ResponseCookie getDeletedCookie() {
        return ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(isSecure)
            .sameSite(isSecure ? "None" : "Lax")
            .path("/")
            .maxAge(0)
            .build();
    }
}
