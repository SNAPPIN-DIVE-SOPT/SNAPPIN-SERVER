package org.sopt.snappinserver.api.v1.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.v1.user.dto.request.CreateOnboardingRequest;
import org.sopt.snappinserver.api.v1.user.dto.response.GetOnboardingResponse;
import org.sopt.snappinserver.api.v1.user.dto.response.GetSwitchedUserProfileResponse;
import org.sopt.snappinserver.api.v1.user.dto.response.GetUserInfoResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "01 - User", description = "사용자 관련 API")
public interface UserApi {

    @Operation(
        summary = "유저 정보 조회 API",
        description = "현재 로그인한 사용자의 역할을 기반으로 사용자 정보를 조회합니다."
    )
    @GetMapping("/me")
    ApiResponseBody<GetUserInfoResponse, Void> getUserInfo(
        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );

    @Operation(
        summary = "유저 프로필 전환 API",
        description = "현재 로그인한 사용자가 유저 프로필 전환이 가능한 경우, 사용자 역할을 전환하여 accessCode를 재발급합니다."
    )
    @PatchMapping("/role")
    ApiResponseBody<GetSwitchedUserProfileResponse, Void> patchUserRole(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Parameter(description = "유저가 로그인한 기기")
        @RequestHeader(value = "User-Agent", required = false)
        String userAgent,

        @Parameter(hidden = true)
        HttpServletResponse httpServletResponse
    );

    @Operation(
        summary = "온보딩 정보 입력 API",
        description = "카카오 로그인 이후, 온보딩 정보가 입력되지 않은 사용자에 한해 추가 정보를 입력받습니다."
    )
    @PostMapping("/onboarding")
    ApiResponseBody<Void, Void> createOnboarding(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @RequestBody
        @Valid
        CreateOnboardingRequest request
    );

    @Operation(
        summary = "온보딩 정보 조회 API",
        description = "예약 문의 화면에서 예약자 정보를 불러올 때 사용합니다."
    )
    @GetMapping("/onboarding")
    ApiResponseBody<GetOnboardingResponse, Void> getOnboarding(
        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );
}
