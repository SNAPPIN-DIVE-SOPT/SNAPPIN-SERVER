package org.sopt.snappinserver.api.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "리뷰 사진 Presigned URL 발급 요청 DTO")
public record PostPresignedUrlRequest(

    @Schema(description = "원본 사진 파일명", example = "review_1.jpg")
    @NotBlank(message = "파일명은 필수입니다.")
    String fileName,

    @Schema(description = "파일 MIME 타입", example = "image/jpg")
    @NotBlank(message = "파일 MIME 타입은 필수입니다.")
    String contentType
) {

}
