package org.sopt.snappinserver.api.v1.user.controller;

import static org.sopt.snappinserver.global.response.code.user.UserSuccessCode.CREATE_ONBOARDING_OK;
import static org.sopt.snappinserver.global.response.code.user.UserSuccessCode.SWITCH_USER_ROLE_OK;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.user.dto.request.CreateOnboardingRequest;
import org.sopt.snappinserver.api.v1.user.dto.response.GetOnboardingResponse;
import org.sopt.snappinserver.api.v1.user.dto.response.GetSwitchedUserProfileResponse;
import org.sopt.snappinserver.api.v1.user.dto.response.GetUserInfoResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.user.service.dto.request.CreateOnboardingCommand;
import org.sopt.snappinserver.domain.user.service.dto.request.SwitchUserRoleCommand;
import org.sopt.snappinserver.domain.user.service.dto.response.GetOnboardingResult;
import org.sopt.snappinserver.domain.user.service.dto.response.GetUserInfoResult;
import org.sopt.snappinserver.domain.user.service.dto.response.SwitchUserRoleResult;
import org.sopt.snappinserver.domain.user.service.usecase.CreateUserOnboardingUseCase;
import org.sopt.snappinserver.domain.user.service.usecase.GetOnboardingUseCase;
import org.sopt.snappinserver.domain.user.service.usecase.GetUserInfoUseCase;
import org.sopt.snappinserver.domain.user.service.usecase.SwitchUserRoleUseCase;
import org.sopt.snappinserver.global.response.code.user.UserSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

    private final GetUserInfoUseCase getUserInfoUseCase;
    private final SwitchUserRoleUseCase switchUserRoleUseCase;
    private final CreateUserOnboardingUseCase createUserOnboardingUseCase;
    private final GetOnboardingUseCase getOnboardingUseCase;

    @Value("${auth.cookie.secure}")
    private boolean isSecure;

    @Value("${jwt.refresh-token-ttl-seconds}")
    private long refreshTokenSeconds;

    @Override
    public ApiResponseBody<GetUserInfoResponse, Void> getUserInfo(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        GetUserInfoResult result = getUserInfoUseCase.getUserInfo(userInfo.userId());
        GetUserInfoResponse response = GetUserInfoResponse.from(result);

        return ApiResponseBody.ok(UserSuccessCode.GET_USER_INFO_OK, response);
    }

    @Override
    public ApiResponseBody<GetSwitchedUserProfileResponse, Void> patchUserRole(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        String userAgent,
        HttpServletResponse httpServletResponse
    ) {
        SwitchUserRoleCommand command = getCommand(userInfo, userAgent);
        SwitchUserRoleResult result = switchUserRoleUseCase.switchUserRole(command);
        GetSwitchedUserProfileResponse response = GetSwitchedUserProfileResponse.from(result);

        ResponseCookie refreshCookie = getResponseCookie(result.refreshToken());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponseBody.ok(SWITCH_USER_ROLE_OK, response);
    }

    @Override
    public ApiResponseBody<Void, Void> createOnboarding(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        CreateOnboardingRequest request
    ) {
        CreateOnboardingCommand command = CreateOnboardingCommand.create(
            userInfo.userId(),
            request
        );
        createUserOnboardingUseCase.createUserOnboarding(command);
        return ApiResponseBody.ok(CREATE_ONBOARDING_OK);
    }

    @Override
    public ApiResponseBody<GetOnboardingResponse, Void> getOnboarding(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        GetOnboardingResult result = getOnboardingUseCase.getOnboarding(userInfo.userId());

        return ApiResponseBody.ok(
            UserSuccessCode.GET_ONBOARDING_OK,
            GetOnboardingResponse.create(result)
        );
    }

    private SwitchUserRoleCommand getCommand(
        CustomUserInfo userInfo,
        String userAgent
    ) {
        return new SwitchUserRoleCommand(userInfo.userId(), userAgent);
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
