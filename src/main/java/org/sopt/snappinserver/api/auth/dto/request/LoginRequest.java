package org.sopt.snappinserver.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 로그인 요청 DTO")
public record LoginRequest(

    @Schema(description = "인가 코드", example = "il3uaRxUwljpgZy_YUYHqR_YhhyqV2WShVDq_k2m-m8TFWMDwlC-kAAAAAQKFwHPAAABm5kN17ctjdRiIM79qQ")
    String code
) {

}
