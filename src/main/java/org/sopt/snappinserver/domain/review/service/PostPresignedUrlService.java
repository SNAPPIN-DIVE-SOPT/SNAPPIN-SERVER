package org.sopt.snappinserver.domain.review.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewErrorCode;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewException;
import org.sopt.snappinserver.domain.review.service.dto.request.PostPresignedUrlCommand;
import org.sopt.snappinserver.domain.review.service.dto.response.PostPresignedUrlResult;
import org.sopt.snappinserver.domain.review.service.usecase.PostPresignedUrlUseCase;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.global.s3.S3Service;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostPresignedUrlService implements PostPresignedUrlUseCase {

    private static final List<String> ALLOWED_EXTENSIONS =
        List.of("jpg", "jpeg", "png", "webp", "heic", "heif");

    private static final List<String> ALLOWED_CONTENT_TYPES =
        List.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/heic",
            "image/heif",
            "application/octet-stream"
        );

    private final UserRepository userRepository;
    private final S3Service s3Service;

    public PostPresignedUrlResult getPresignedUrlForUpload(PostPresignedUrlCommand command) {
        User user = getExistingUser(command);
        validateUser(command, user);

        String s3Key = generateStoredFileName(command.fileName());
        String uploadUrl = s3Service.getUploadPresignedUrl(s3Key, command.contentType());
        String imageUrl = s3Service.getPresignedUrl(s3Key);

        return new PostPresignedUrlResult(uploadUrl, imageUrl, s3Key);
    }

    private void validateUser(PostPresignedUrlCommand command, User user) {
        validateIsClientUser(user);
        validateContentType(command.contentType());
    }

    private User getExistingUser(PostPresignedUrlCommand command) {
        return userRepository.findById(command.userId())
            .orElseThrow(() -> new ReviewException(ReviewErrorCode.USER_NOT_FOUND));
    }

    private void validateIsClientUser(User user) {
        if(!user.isLoginByClient()) {
            throw new ReviewException(ReviewErrorCode.USER_MUST_LOGIN_BY_CLIENT);
        }
    }

    private void validateContentType(String contentType) {
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ReviewException(ReviewErrorCode.UNSUPPORTED_IMAGE_TYPE);
        }
    }

    private String generateStoredFileName(String originalFileName) {
        String extension = extractExtension(originalFileName);
        return "review/" + UUID.randomUUID() + extension;
    }

    private String extractExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        validateIsValidFileName(index);

        String extension = fileName.substring(index + 1).toLowerCase();
        validateIsAllowedExtensions(extension);

        return "." + extension;
    }

    private void validateIsValidFileName(int index) {
        if (index == -1) {
            throw new ReviewException(ReviewErrorCode.INVALID_FILE_NAME);
        }
    }

    private void validateIsAllowedExtensions(String extension) {
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ReviewException(ReviewErrorCode.UNSUPPORTED_IMAGE_TYPE);
        }
    }

}
