package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.UploadConsentInfoResult;

@Schema(description = "업로드 동의 안내")
public record UploadConsentResponse(

    @Schema(description = "동의 시 안내", example = "보정본 1장 추가 제공")
    String agreeNote,

    @Schema(description = "비동의 시 안내", example = "동영상 제공 불가")
    String disagreeNote
) {

    public static UploadConsentResponse from(UploadConsentInfoResult result) {
        return new UploadConsentResponse(result.agreeNote(), result.disagreeNote());
    }
}
