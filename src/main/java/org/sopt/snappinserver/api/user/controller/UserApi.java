package org.sopt.snappinserver.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.snappinserver.api.user.dto.response.GetUserInfoResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;

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
}
