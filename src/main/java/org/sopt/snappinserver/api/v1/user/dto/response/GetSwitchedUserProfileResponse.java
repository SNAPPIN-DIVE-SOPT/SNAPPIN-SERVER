package org.sopt.snappinserver.api.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.user.service.dto.response.SwitchUserRoleResult;

@Schema(description = "유저 프로필 전환 API 응답 DTO")
public record GetSwitchedUserProfileResponse(

    @Schema(description = "새로 발급된 AccessToken")
    String accessToken,

    @Schema(description = "전환된 유저 역할")
    String role
) {

    public static GetSwitchedUserProfileResponse from(SwitchUserRoleResult result) {
        return new GetSwitchedUserProfileResponse(result.accessToken(), result.role());
    }
}
