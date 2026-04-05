package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;

public record AdditionalRequestSectionResult(
    String title,
    List<String> content
) {

}
