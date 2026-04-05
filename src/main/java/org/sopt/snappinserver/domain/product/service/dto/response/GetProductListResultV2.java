package org.sopt.snappinserver.domain.product.service.dto.response;

import java.util.List;

public record GetProductListResultV2(
    List<GetProductCardResultV2> products,
    GetProductListMetaV2 meta
) {

}
