package org.sopt.snappinserver.api.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.auth.code.AuthSuccessCode;
import org.sopt.snappinserver.api.auth.dto.request.LoginRequest;
import org.sopt.snappinserver.api.auth.dto.response.LoginResponse;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;
import org.sopt.snappinserver.domain.auth.service.usecase.LoginUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController implements AuthApi {

    private static final int REFRESH_TOKEN_MAX_AGE = 14 * 24 * 60 * 60;

    private final LoginUseCase loginUseCase;

    @Override
    @PostMapping("/login/kakao")
    public ApiResponseBody<LoginResponse, Void> kakaoLogin(
        @RequestBody LoginRequest loginRequest,
        @RequestHeader(value = "User-Agent", required = false) String userAgent,
        HttpServletResponse httpServletResponse
    ) {
        LoginResult loginResult = loginUseCase.kakaoLogin(loginRequest.code(), userAgent);
        ResponseCookie refreshCookie = getResponseCookie(loginResult);
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponseBody.ok(
            AuthSuccessCode.LOGIN_SUCCESS,
            new LoginResponse(loginResult.accessToken())
        );
    }

    private ResponseCookie getResponseCookie(LoginResult loginResult) {
        return ResponseCookie.from("refreshToken", loginResult.refreshToken())
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .path("/")
            .maxAge(REFRESH_TOKEN_MAX_AGE)
            .build();
    }

}
