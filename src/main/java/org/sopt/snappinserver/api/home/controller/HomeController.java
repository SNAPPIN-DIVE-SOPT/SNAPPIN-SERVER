package org.sopt.snappinserver.api.home.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.home.code.HomeSuccessCode;
import org.sopt.snappinserver.api.home.dto.response.GetPlacePhotographerRecommendationResponse;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetRandomPhotographersResult;
import org.sopt.snappinserver.domain.photographer.service.usecase.GetRandomPhotographersUseCase;
import org.sopt.snappinserver.domain.place.service.dto.response.GetRecommendationPlaceResult;
import org.sopt.snappinserver.domain.place.service.usecase.GetRecommendationPlaceUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
@RestController
public class HomeController implements HomeApi {

    private final GetRecommendationPlaceUseCase getRecommendationPlaceUseCase;
    private final GetRandomPhotographersUseCase getRandomPhotographersUseCase;

    @Override
    @GetMapping("/recommendation")
    public ApiResponseBody<GetPlacePhotographerRecommendationResponse, Void> getRecommendation() {
        List<GetRecommendationPlaceResult> placeResult = getRecommendationPlaceUseCase
            .getPlaceRecommendation();
        List<GetRandomPhotographersResult> photographersResults = getRandomPhotographersUseCase
            .getRandomPhotographersResult();
        GetPlacePhotographerRecommendationResponse response =
            GetPlacePhotographerRecommendationResponse.of(placeResult, photographersResults);

        return ApiResponseBody.ok(
            HomeSuccessCode.GET_PLACE_PHOTOGRAPHER_RECOMMENDATION_OK,
            response
        );
    }

}
