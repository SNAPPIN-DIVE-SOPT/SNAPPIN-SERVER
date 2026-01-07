package org.sopt.snappinserver.domain.product.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductReviewPageResult {

    private List<ProductReviewResult> reviews;
    private Long nextCursor;
    private boolean hasNext;
}