package org.sopt.snappinserver.domain.wish.service.dto.response;

import java.util.List;

public record WishedProductsPageResult(
    List<WishedProductResult> products,
    Long nextCursor,
    boolean hasNext
) {

    public static WishedProductsPageResult from(
        List<WishedProductResult> products,
        Long nextCursor,
        boolean hasNext
    ) {
        return new WishedProductsPageResult(products, nextCursor, hasNext);
    }
}
