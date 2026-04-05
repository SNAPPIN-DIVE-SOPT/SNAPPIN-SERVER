package org.sopt.snappinserver.api.v2.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.sopt.snappinserver.api.v2.product.dto.response.GetProductDetailResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "06 - Product", description = "상품 관련 API")
public interface ProductApi {

    @Operation(
        summary = "상품 상세 정보 및 상품 안내 조회 API",
        description = "상품 안내를 포함한 상품 상세 정보를 조회합니다. 좋아요 수, 장수 추가 가능 여부, 유료 옵션 목록이 추가되었습니다."
    )
    @GetMapping("/{productId}")
    ApiResponseBody<GetProductDetailResponse, Void> getProductDetail(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "상품 ID")
        @NotNull(message = "상품은 비어있을 수 없습니다.")
        @PathVariable Long productId
    );
}
