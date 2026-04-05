package org.sopt.snappinserver.domain.product.service.dto.request;

import java.util.List;

public record CreateProductReviewCommand(
    Long userId,
    Long productId,
    Integer rating,
    String content,
    List<String> imageUrls
) {

}
