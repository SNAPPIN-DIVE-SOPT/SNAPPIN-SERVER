package org.sopt.snappinserver.api.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.user.service.dto.response.GetUserInfoResult;

@Schema(description = "유저 정보 조회 응답 DTO")
public record GetUserInfoResponse(

    @Schema(description = "유저 ID")
    Long id,

    @Schema(description = "현재 로그인된 유저 역할")
    String role,

    @Schema(description = "유저 프로필 이미지")
    String profileImageUrl,

    @Schema(description = "작가 프로필 보유 여부")
    boolean hasPhotographerProfile,

    @Schema(description = "고객 관련 정보 DTO")
    GetClientInfoResponse clientInfo,

    @Schema(description = "작가 프로필 관련 정보 DTO")
    GetPhotographerInfoResponse photographerInfo
) {

    public static GetUserInfoResponse from(GetUserInfoResult result) {
        return new GetUserInfoResponse(
            result.id(),
            result.role(),
            result.profileImageUrl(),
            result.hasPhotographerProfile(),
            result.clientInfo() != null
                ? GetClientInfoResponse.from(result.clientInfo())
                : null,
            result.photographerInfo() != null
                ? GetPhotographerInfoResponse.from(result.photographerInfo())
                : null
        );
    }
}
