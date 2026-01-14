package org.sopt.snappinserver.domain.review.service.dto.response;

public record PostPresignedUrlResult(
    String uploadUrl,
    String imageUrl,
    String s3Key
) {

}
