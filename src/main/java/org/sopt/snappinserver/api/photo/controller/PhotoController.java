package org.sopt.snappinserver.api.photo.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.photo.code.PhotoSuccessCode;
import org.sopt.snappinserver.api.photo.dto.request.PhotoProcessRequest;
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

    @PostMapping("/process")
    public ApiResponseBody<Void, Void> processPhoto(
        @RequestBody PhotoProcessRequest photoProcessRequest
    ) {
        PhotoProcessCommand photoProcessCommand = PhotoProcessCommand.from(photoProcessRequest);
        processPhotoUseCase.processPhoto(photoProcessCommand);

        return ApiResponseBody.ok(PhotoSuccessCode.PHOTO_MOOD_CREATED);
    }

}
