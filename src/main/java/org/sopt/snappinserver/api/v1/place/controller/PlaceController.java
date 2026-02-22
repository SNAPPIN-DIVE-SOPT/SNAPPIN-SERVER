package org.sopt.snappinserver.api.v1.place.controller;

import static org.sopt.snappinserver.global.response.code.place.PlaceSuccessCode.GET_PLACE_LIST_OK;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.place.dto.response.GetPlaceListResponse;
import org.sopt.snappinserver.domain.place.service.dto.response.GetPlaceListResult;
import org.sopt.snappinserver.domain.place.service.usecase.GetPlaceListUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
@RestController
public class PlaceController implements PlaceApi {

    private final GetPlaceListUseCase getPlaceListUseCase;

    @Override
    public ApiResponseBody<GetPlaceListResponse, Void> getPlaces(
        String keyword
    ) {
        GetPlaceListResult result = getPlaceListUseCase.getPlaceList(keyword);
        GetPlaceListResponse response = GetPlaceListResponse.from(result);

        return ApiResponseBody.ok(GET_PLACE_LIST_OK, response);
    }
}
