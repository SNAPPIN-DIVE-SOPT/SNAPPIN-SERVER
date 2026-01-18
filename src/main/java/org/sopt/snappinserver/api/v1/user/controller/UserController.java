package org.sopt.snappinserver.api.v1.user.controller;

import static org.sopt.snappinserver.global.response.code.user.UserSuccessCode.SWITCH_USER_ROLE_OK;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.user.UserSuccessCode;
import org.sopt.snappinserver.api.v1.user.dto.response.GetSwitchedUserProfileResponse;
import org.sopt.snappinserver.api.v1.user.dto.response.GetUserInfoResponse;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.user.service.dto.request.SwitchUserRoleCommand;
import org.sopt.snappinserver.domain.user.service.dto.response.GetUserInfoResult;
import org.sopt.snappinserver.domain.user.service.dto.response.SwitchUserRoleResult;
import org.sopt.snappinserver.domain.user.service.usecase.GetUserInfoUseCase;
import org.sopt.snappinserver.domain.user.service.usecase.SwitchUserRoleUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

    private final GetUserInfoUseCase getUserInfoUseCase;
    private final SwitchUserRoleUseCase switchUserRoleUseCase;

    @Value("${auth.cookie.secure}")
    private boolean isSecure;

    @Value("${jwt.refresh-token-ttl-seconds}")
    private long refreshTokenSeconds;

    @Override
    @GetMapping("/me")
    public ApiResponseBody<GetUserInfoResponse, Void> getUserInfo(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        GetUserInfoResult result = getUserInfoUseCase.getUserInfo(userInfo.userId());
        GetUserInfoResponse response = GetUserInfoResponse.from(result);

        return ApiResponseBody.ok(UserSuccessCode.GET_USER_INFO_OK, response);
    }

    @Override
    @PatchMapping("/role")
    public ApiResponseBody<GetSwitchedUserProfileResponse, Void> patchUserRole(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        @CookieValue(name = "refreshToken") String refreshToken,
        @RequestHeader(value = "User-Agent", required = false) String userAgent,
        HttpServletResponse httpServletResponse
    ) {
        validateCookieExists(refreshToken);

        SwitchUserRoleCommand command = getCommand(userInfo, refreshToken, userAgent);
        SwitchUserRoleResult result = switchUserRoleUseCase.switchUserRole(command);
        GetSwitchedUserProfileResponse response = GetSwitchedUserProfileResponse.from(result);

        ResponseCookie refreshCookie = getResponseCookie(result.refreshToken());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponseBody.ok(SWITCH_USER_ROLE_OK, response);
    }

    private SwitchUserRoleCommand getCommand(
        CustomUserInfo userInfo,
        String refreshToken,
        String userAgent
    ) {
        return new SwitchUserRoleCommand(userInfo.userId(), refreshToken, userAgent);
    }

    private void validateCookieExists(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_COOKIE_REQUIRED);
        }
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
