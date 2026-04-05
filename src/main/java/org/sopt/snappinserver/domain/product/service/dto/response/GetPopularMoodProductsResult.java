package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;

public record GetPopularMoodProductsResult(
    String mood,
    List<PopularMoodProductItemResult> products
) {

}
