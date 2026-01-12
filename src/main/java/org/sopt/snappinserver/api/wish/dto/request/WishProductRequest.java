package org.sopt.snappinserver.api.wish.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "상품 좋아요/취소 요청 DTO")
public record WishProductRequest(
    @Schema(description = "좋아요/취소할 상품 아이디", example = "1")
    @NotNull(message = "productId는 필수입니다.")
    Long productId
) {

}
