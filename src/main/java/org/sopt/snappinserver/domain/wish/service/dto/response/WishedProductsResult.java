package org.sopt.snappinserver.domain.wish.service.dto.response;

import java.util.List;

public record WishedProductsResult(List<WishedProductResult> products) {

    public static WishedProductsResult from(List<WishedProductResult> products) {
        return new WishedProductsResult(products);
    }
}
