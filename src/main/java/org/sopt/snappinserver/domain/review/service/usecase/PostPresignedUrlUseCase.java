package org.sopt.snappinserver.domain.review.service.usecase;

import org.sopt.snappinserver.domain.review.service.dto.request.PostPresignedUrlCommand;
import org.sopt.snappinserver.domain.review.service.dto.response.PostPresignedUrlResult;

public interface PostPresignedUrlUseCase {

    PostPresignedUrlResult getPresignedUrlForUpload(PostPresignedUrlCommand command);
}
