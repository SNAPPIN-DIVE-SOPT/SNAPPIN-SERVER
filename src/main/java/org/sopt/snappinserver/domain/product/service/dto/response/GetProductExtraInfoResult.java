package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;

public record GetProductExtraInfoResult(
    UploadConsentInfoResult uploadConsent,
    List<AdditionalRequestSectionResult> additionalRequest
) {

}
