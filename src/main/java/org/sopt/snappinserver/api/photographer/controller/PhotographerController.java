package org.sopt.snappinserver.api.photographer.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.photographer.code.PhotographerSuccessCode;
import org.sopt.snappinserver.api.photographer.dto.response.GetPhotographerProfileResponse;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerProfileResult;
import org.sopt.snappinserver.domain.photographer.service.dto.usecase.GetPhotographerProfileUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/photographers")
@RequiredArgsConstructor
@RestController
public class PhotographerController implements PhotographerApi {

    private final GetPhotographerProfileUseCase getPhotographerProfileUseCase;

    @Override
    @GetMapping("/{photographerId}")
    public ApiResponseBody<GetPhotographerProfileResponse, Void> getPhotographerProfile(
        Long photographerId
    ) {
        GetPhotographerProfileResult result = getPhotographerProfileUseCase
            .getPhotographerProfile(photographerId);
        GetPhotographerProfileResponse response = GetPhotographerProfileResponse.from(result);

        return ApiResponseBody.ok(PhotographerSuccessCode.GET_PHOTOGRAPHER_PROFILE_OK, response);
    }
}
