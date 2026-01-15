package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;

public record GetProductListResult(
    List<GetProductCardResult> products,
    CursorMeta cursorMeta
) {

}
