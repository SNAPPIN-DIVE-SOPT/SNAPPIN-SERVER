package org.sopt.snappinserver.api.photo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.photo.code.PhotoSuccessCode;
import org.sopt.snappinserver.api.photo.dto.request.CreatePhotoMoodRequest;
import org.sopt.snappinserver.domain.photo.service.dto.request.PhotoProcessCommand;
import org.sopt.snappinserver.domain.photo.service.usecase.ProcessPhotoUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
@RestController
public class PhotoController implements PhotoApi {

    private final ProcessPhotoUseCase processPhotoUseCase;

    @Override
    @PostMapping("/process")
    public ApiResponseBody<Void, Void> createPhotoMoodConnection(
        @Valid @RequestBody CreatePhotoMoodRequest createPhotoMoodRequest
    ) {
        PhotoProcessCommand photoProcessCommand = PhotoProcessCommand.from(createPhotoMoodRequest);
        processPhotoUseCase.linkPhotoWithMoodTags(photoProcessCommand);

        return ApiResponseBody.ok(PhotoSuccessCode.PHOTO_MOOD_CREATED);
    }

}
