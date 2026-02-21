package org.sopt.snappinserver.api.v2.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginWithPhotographerProfileResult;

@Schema(description = "카카오 로그인 응답 DTO")
public record CreateKakaoLoginResponse(

    @Schema(description = "신규 가입 여부")
    boolean isNew,

    @Schema(description = "인증 시 필요한 accessToken입니다. refreshToken은 쿠키로 내려드립니다.")
    String accessToken,

    @Schema(description = "현재 로그인한 유저 역할")
    String role,

    @Schema(description = "작가 프로필 보유 여부")
    boolean hasPhotographerProfile
) {

    public static CreateKakaoLoginResponse from(LoginWithPhotographerProfileResult loginResult) {
        return new CreateKakaoLoginResponse(
            loginResult.isNew(),
            loginResult.accessToken(),
            loginResult.role(),
            loginResult.hasPhotographerProfile()
        );
    }
}
