package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.AdditionalRequestSectionResult;

@Schema(description = "기타 요청 사항 섹션")
public record AdditionalRequestSectionResponse(

    @Schema(description = "섹션 제목", example = "학위복 대여안내")
    String title,

    @Schema(description = "항목 목록")
    List<String> content
) {

    public static AdditionalRequestSectionResponse from(AdditionalRequestSectionResult result) {
        return new AdditionalRequestSectionResponse(result.title(), result.content());
    }
}
