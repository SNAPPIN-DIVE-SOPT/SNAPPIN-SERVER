package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.request.ProductReservationCommand;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReservationResult;

public interface PostProductReservationUseCase {

    ProductReservationResult reserve(
        Long productId,
        Long userId,
        ProductReservationCommand command
    );
}
