package org.sopt.snappinserver.api.v2.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.v2.auth.dto.request.CreateKakaoLoginRequest;
import org.sopt.snappinserver.api.v2.auth.dto.response.CreateKakaoLoginResponse;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "01 - Auth", description = "인증/인가 관련 API V2")
public interface AuthApi {

    @PostMapping("/login/kakao")
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
}
