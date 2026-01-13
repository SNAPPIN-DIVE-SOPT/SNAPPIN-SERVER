package org.sopt.snappinserver.api.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.review.service.dto.response.PostPresignedUrlResult;

@Schema(description = "리뷰 이미지 URL 발급 응답 DTO")
public record PostPresignedUrlResponse(

    @Schema(description = "리뷰 이미지를 등록할 Presigned URL")
    String uploadUrl,

    @Schema(description = "리뷰 이미지 미리보기 조회 시 필요한 Presigned URL")
    String imageUrl,

    @Schema(description = "리뷰 이미지 S3 Key")
    String s3Key
) {

    public static PostPresignedUrlResponse from(PostPresignedUrlResult result) {
        return new PostPresignedUrlResponse(
            result.uploadUrl(),
            result.imageUrl(),
            result.s3Key()
        );
    }
}
