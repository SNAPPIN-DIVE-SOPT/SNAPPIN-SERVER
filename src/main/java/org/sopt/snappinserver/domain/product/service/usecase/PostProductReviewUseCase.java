package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.request.CreateProductReviewCommand;
import org.sopt.snappinserver.domain.product.service.dto.response.CreateProductReviewResult;

public interface PostProductReviewUseCase {

    CreateProductReviewResult createProductReview(CreateProductReviewCommand command);
}
