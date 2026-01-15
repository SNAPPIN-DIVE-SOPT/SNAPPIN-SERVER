package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;

public record GetProductCardResult(
    Long id,
    String imageUrl,
    String title,
    Double rate,
    long reviewCount,
    String photographer,
    int price,
    List<String> moods
) {

}
