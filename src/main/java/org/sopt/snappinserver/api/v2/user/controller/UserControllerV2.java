package org.sopt.snappinserver.api.v2.user.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v2.user.dto.response.GetUserInfoResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.user.service.dto.response.GetUserInfoResult;
import org.sopt.snappinserver.domain.user.service.usecase.GetUserInfoUseCase;
import org.sopt.snappinserver.global.response.code.user.UserSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
@RestController
public class UserControllerV2 implements UserApi{

    private final GetUserInfoUseCase getUserInfoUseCase;

    @Override
    public ApiResponseBody<GetUserInfoResponse, Void> getUserInfo(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        GetUserInfoResult result = getUserInfoUseCase.getUserInfo(userInfo.userId());
        GetUserInfoResponse response = GetUserInfoResponse.from(result);

        return ApiResponseBody.ok(UserSuccessCode.GET_USER_INFO_OK, response);
    }
}
