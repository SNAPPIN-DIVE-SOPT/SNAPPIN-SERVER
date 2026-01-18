package org.sopt.snappinserver.api.v1.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.sopt.snappinserver.api.v1.review.dto.request.PostPresignedUrlRequest;
import org.sopt.snappinserver.api.v1.review.dto.response.GetReviewDetailResponse;
import org.sopt.snappinserver.api.v1.review.dto.response.PostPresignedUrlResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "011 - Review", description = "리뷰 관련 API")
public interface ReviewApi {

    @Operation(
        summary = "리뷰 사진 url 발급 API",
        description = "리뷰에 업로드하는 사진을 저장할 S3 Presigned URL을 생성하여 반환합니다."
    )
    ApiResponseBody<PostPresignedUrlResponse, Void> postPresignedUrl(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Valid @RequestBody PostPresignedUrlRequest request
    );

    @Operation(
        summary = "리뷰 상세 조회",
        description = "상품 상세 조회와 고객/작가 예약에서 리뷰 상세 정보를 조회합니다."
    )
    @GetMapping("/{reviewId}")
    ApiResponseBody<GetReviewDetailResponse, Void> getReviewDetail(
        @Schema(description = "리뷰 ID")
        @PathVariable @NotNull Long reviewId
    );
}
