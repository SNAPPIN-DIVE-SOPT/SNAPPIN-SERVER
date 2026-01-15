package org.sopt.snappinserver.domain.product.service.dto.response;

public record CursorMeta(
    Long nextCursor,
    boolean hasNext
) {

}
