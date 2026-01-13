package org.sopt.snappinserver.api.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 프로필 전환 API 응답 DTO")
public record GetSwitchedUserProfileResponse(

    @Schema(description = "새로 발급된 AccessCode")
    String accessCode
) {

}
