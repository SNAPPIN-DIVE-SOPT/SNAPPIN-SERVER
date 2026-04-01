package org.sopt.snappinserver.api.v1.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.user.service.dto.response.GetOnboardingResult;

@Schema(description = "온보딩 조회 응답")
public record GetOnboardingResponse(

    @Schema(description = "온보딩 시 작성한 이름")
    String name,

    @Schema(description = "온보딩 시 작성한 전화번호")
    String phoneNumber,

    @Schema(description = "온보딩 시 작성한 이메일")
    String email
) {

    public static GetOnboardingResponse create(GetOnboardingResult result) {
        return new GetOnboardingResponse(
            result.name(),
            result.phoneNumber(),
            result.email()
        );
    }
}
