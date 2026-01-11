package org.sopt.snappinserver.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.user.code.UserSuccessCode;
import org.sopt.snappinserver.api.user.dto.response.GetUserInfoResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.user.service.GetUserInfoService;
import org.sopt.snappinserver.domain.user.service.dto.response.GetUserInfoResult;
import org.sopt.snappinserver.domain.user.service.usecase.GetUserInfoUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

    private final GetUserInfoUseCase getUserInfoUseCase;
    private final GetUserInfoService getUserInfoService;

    @Override
    @GetMapping("/me")
    public ApiResponseBody<GetUserInfoResponse, Void> getUserInfo(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        GetUserInfoResult result = getUserInfoService.getUserInfo(userInfo.userId());
        GetUserInfoResponse response = GetUserInfoResponse.from(result);

        return ApiResponseBody.ok(UserSuccessCode.GET_USER_INFO_OK, response);
    }
}
