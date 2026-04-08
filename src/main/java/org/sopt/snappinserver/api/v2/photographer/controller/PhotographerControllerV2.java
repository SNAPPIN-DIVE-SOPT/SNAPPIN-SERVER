package org.sopt.snappinserver.api.v2.photographer.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v2.photographer.dto.response.GetPhotographerProfileResponseV2;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerProfileResult;
import org.sopt.snappinserver.domain.photographer.service.usecase.GetPhotographerProfileUseCase;
import org.sopt.snappinserver.global.response.code.photographer.PhotographerSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/photographers")
@RequiredArgsConstructor
@RestController
public class PhotographerControllerV2 implements PhotographerApiV2 {

    private final GetPhotographerProfileUseCase getPhotographerProfileUseCase;

    @Override
    public ApiResponseBody<GetPhotographerProfileResponseV2, Void> getPhotographerProfile(
        Long photographerId
    ) {
        GetPhotographerProfileResult result = getPhotographerProfileUseCase
            .getPhotographerProfile(photographerId);
        GetPhotographerProfileResponseV2 response = GetPhotographerProfileResponseV2.from(result);

        return ApiResponseBody.ok(PhotographerSuccessCode.GET_PHOTOGRAPHER_PROFILE_OK, response);
    }
}
