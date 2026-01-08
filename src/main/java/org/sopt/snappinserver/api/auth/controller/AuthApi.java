package org.sopt.snappinserver.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.auth.dto.request.LoginRequest;
import org.sopt.snappinserver.api.auth.dto.response.LoginResponse;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "01 - Auth", description = "인증/인가 관련 API")
public interface AuthApi {

    @Operation(
        summary = "카카오 로그인",
        description = "인가 코드를 받아 카카오로 소셜 로그인을 진행합니다."
    )
    ApiResponseBody<LoginResponse, Void> createKakaoLogin(
        @Valid @RequestBody LoginRequest loginRequest,
        @RequestHeader(value = "User-Agent", required = false) String userAgent,
        HttpServletResponse httpServletResponse
    );

}
