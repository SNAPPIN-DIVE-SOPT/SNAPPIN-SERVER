package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;

public record ProductReviewPageResult(
    List<ProductReviewResult> reviews,
    Long nextCursor,
    boolean hasNext
) {}