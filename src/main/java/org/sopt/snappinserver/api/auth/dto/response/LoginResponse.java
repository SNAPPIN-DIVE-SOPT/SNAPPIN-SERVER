package org.sopt.snappinserver.api.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 로그인 응답 DTO")
public record LoginResponse(

    @Schema(description = "인증 시 필요한 accessToken입니다. refreshToken은 쿠키로 내려드립니다.")
    String accessToken
) {

}
