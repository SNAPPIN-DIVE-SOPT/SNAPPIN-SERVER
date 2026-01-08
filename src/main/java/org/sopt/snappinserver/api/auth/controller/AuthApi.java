package org.sopt.snappinserver.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.auth.dto.request.CreateKakaoLoginRequest;
import org.sopt.snappinserver.api.auth.dto.response.CreateAccessTokenResponse;
import org.sopt.snappinserver.api.auth.dto.response.CreateKakaoLoginResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "01 - Auth", description = "인증/인가 관련 API")
public interface AuthApi {

    @Operation(
        summary = "카카오 로그인",
        description = "인가 코드를 받아 카카오로 소셜 로그인을 진행합니다."
    )
    ApiResponseBody<CreateKakaoLoginResponse, Void> createKakaoLogin(

        @Schema(description = "카카오에 등록할 redirect_uri 주소입니다.", example = "http://localhost:8080/api/v1/auth/login/kakao", nullable = true)
        @RequestParam(name = "redirect_uri", required = false) String clientRedirectUri,

        @Valid @RequestBody CreateKakaoLoginRequest createKakaoLoginRequest,

        @RequestHeader(value = "User-Agent", required = false) String userAgent,

        HttpServletResponse httpServletResponse
    );

    @Operation(
        summary = "토큰 재발급",
        description = "accessToken 만료 시, 기존 Refresh Token 으로 새로운 accessToken 을 반환하고, 새로운 refreshToken 으로 쿠키를 교체합니다."
    )
    ApiResponseBody<CreateAccessTokenResponse, Void> createRefreshedAccessToken(

        @Schema(description = "재발급 때 사용할 refreshToken 입니다. 쿠키 설정만 해주시면 자동으로 보내집니다.")
        @CookieValue(name = "refreshToken") String refreshToken,

        @RequestHeader(value = "User-Agent", required = false) String userAgent,

        HttpServletResponse httpServletResponse
    );

    @Operation(
        summary = "로그아웃",
        description = "accessToken, refreshToken 을 받아 사용자를 로그아웃 처리합니다."
    )
    ApiResponseBody<Void, Void> logout(
        @AuthenticationPrincipal CustomUserInfo principal,
        @CookieValue(name = "refreshToken", required = false) String refreshToken,
        HttpServletResponse response
    );

}
