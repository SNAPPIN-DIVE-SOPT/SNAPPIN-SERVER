package org.sopt.snappinserver.api.v1.review.controller;

import static org.sopt.snappinserver.api.v1.review.code.ReviewSuccessCode.POST_PRESIGNED_URL_OK;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.review.code.ReviewSuccessCode;
import org.sopt.snappinserver.api.v1.review.dto.request.PostPresignedUrlRequest;
import org.sopt.snappinserver.api.v1.review.dto.response.GetReviewDetailResponse;
import org.sopt.snappinserver.api.v1.review.dto.response.PostPresignedUrlResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.review.service.dto.request.PostPresignedUrlCommand;
import org.sopt.snappinserver.domain.review.service.dto.response.GetReviewDetailResult;
import org.sopt.snappinserver.domain.review.service.dto.response.PostPresignedUrlResult;
import org.sopt.snappinserver.domain.review.service.usecase.GetReviewDetailUseCase;
import org.sopt.snappinserver.domain.review.service.usecase.PostPresignedUrlUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController implements ReviewApi {

    private final PostPresignedUrlUseCase postPresignedUrlUseCase;
    private final GetReviewDetailUseCase getReviewDetailUseCase;

    @Override
    @PostMapping("/image")
    public ApiResponseBody<PostPresignedUrlResponse, Void> postPresignedUrl(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        @Valid @RequestBody PostPresignedUrlRequest request
    ) {
        PostPresignedUrlCommand command = getPostPresignedUrlCommand(userInfo, request);
        PostPresignedUrlResult result = postPresignedUrlUseCase.getPresignedUrlForUpload(command);
        PostPresignedUrlResponse response = PostPresignedUrlResponse.from(result);

        return ApiResponseBody.ok(POST_PRESIGNED_URL_OK, response);
    }

    @Override
    public ApiResponseBody<GetReviewDetailResponse, Void> getReviewDetail(
        Long reviewId
    ) {
        GetReviewDetailResult result =
            getReviewDetailUseCase.getReviewDetail(reviewId);
        GetReviewDetailResponse response = GetReviewDetailResponse.from(result);

        return ApiResponseBody.ok(ReviewSuccessCode.GET_REVIEW_DETAIL_OK, response);
    }


    private PostPresignedUrlCommand getPostPresignedUrlCommand(
        CustomUserInfo userInfo,
        PostPresignedUrlRequest request
    ) {
        return new PostPresignedUrlCommand(
            userInfo.userId(),
            request.fileName(),
            request.contentType()
        );
    }
}
