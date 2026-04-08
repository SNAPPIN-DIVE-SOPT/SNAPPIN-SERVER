package org.sopt.snappinserver.domain.product.service.dto.response;

public record GetProductListMetaV2(
    boolean hasNext,
    String nextCursor,
    long totalCount
) {

}
