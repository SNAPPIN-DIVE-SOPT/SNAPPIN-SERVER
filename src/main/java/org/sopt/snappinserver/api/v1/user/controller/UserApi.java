package org.sopt.snappinserver.api.v1.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.sopt.snappinserver.api.v1.user.dto.response.GetSwitchedUserProfileResponse;
import org.sopt.snappinserver.api.v1.user.dto.response.GetUserInfoResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "01 - User", description = "사용자 관련 API")
public interface UserApi {

    @Operation(
        summary = "유저 정보 조회 API",
        description = "현재 로그인한 사용자의 역할을 기반으로 사용자 정보를 조회합니다."
    )
    ApiResponseBody<GetUserInfoResponse, Void> getUserInfo(
        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );

    @Operation(
        summary = "유저 프로필 전환 API",
        description = "현재 로그인한 사용자가 유저 프로필 전환이 가능한 경우, 사용자 역할을 전환하여 accessCode를 재발급합니다."
    )
    ApiResponseBody<GetSwitchedUserProfileResponse, Void> patchUserRole(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "재발급 때 사용할 refreshToken 입니다. 쿠키 설정만 해주시면 자동으로 보내집니다.")
        @CookieValue(name = "refreshToken") String refreshToken,

        @RequestHeader(value = "User-Agent", required = false) String userAgent,

        HttpServletResponse httpServletResponse
    );
}
