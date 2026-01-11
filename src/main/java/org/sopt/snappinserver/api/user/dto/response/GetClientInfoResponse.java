package org.sopt.snappinserver.api.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.user.service.dto.response.GetClientInfoResult;

@Schema(description = "고객 정보 응답 DTO")
public record GetClientInfoResponse(

    @Schema(description = "고객명")
    String name,

    @Schema(description = "고객 큐레이션 무드 결과 목록")
    List<String> curatedMoods
) {

    public static GetClientInfoResponse from(GetClientInfoResult result) {
        return new GetClientInfoResponse(
            result.name(),
            result.curatedMoods()
        );
    }
}
