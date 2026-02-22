package org.sopt.snappinserver.api.v1.photographer.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.photographer.dto.response.GetPhotographerProfileResponse;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerProfileResult;
import org.sopt.snappinserver.domain.photographer.service.usecase.GetPhotographerProfileUseCase;
import org.sopt.snappinserver.global.response.code.photographer.PhotographerSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/photographers")
@RequiredArgsConstructor
@RestController
public class PhotographerController implements PhotographerApi {

    private final GetPhotographerProfileUseCase getPhotographerProfileUseCase;

    @Override
    public ApiResponseBody<GetPhotographerProfileResponse, Void> getPhotographerProfile(
        @NotNull(message = "작가 ID는 필수입니다.")
        @Positive(message = "작가 ID는 양수여야 합니다.")
        Long photographerId
    ) {
        GetPhotographerProfileResult result = getPhotographerProfileUseCase
            .getPhotographerProfile(photographerId);
        GetPhotographerProfileResponse response = GetPhotographerProfileResponse.from(result);

        return ApiResponseBody.ok(PhotographerSuccessCode.GET_PHOTOGRAPHER_PROFILE_OK, response);
    }
}
