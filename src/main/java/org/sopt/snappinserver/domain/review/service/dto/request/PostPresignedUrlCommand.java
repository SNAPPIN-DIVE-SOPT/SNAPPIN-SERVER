package org.sopt.snappinserver.domain.review.service.dto.request;

public record PostPresignedUrlCommand(
    Long userId,
    String fileName,
    String contentType
) {

}
