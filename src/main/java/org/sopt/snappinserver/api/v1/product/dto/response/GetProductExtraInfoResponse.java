package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductExtraInfoResult;

@Schema(description = "상품 예약 부가 안내 응답")
public record GetProductExtraInfoResponse(

    @Schema(description = "업로드 동의 안내")
    UploadConsentResponse uploadConsent,

    @Schema(description = "기타 요청 사항")
    List<AdditionalRequestSectionResponse> additionalRequest
) {

    public static GetProductExtraInfoResponse from(GetProductExtraInfoResult result) {
        return new GetProductExtraInfoResponse(
            UploadConsentResponse.from(result.uploadConsent()),
            result.additionalRequest().stream()
                .map(AdditionalRequestSectionResponse::from)
                .toList()
        );
    }
}
