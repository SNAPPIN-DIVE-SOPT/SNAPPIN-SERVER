package org.sopt.snappinserver.api.v2.mood.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v2.mood.dto.response.GetMoodFilterListResponse;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterListResult;
import org.sopt.snappinserver.domain.mood.service.usecase.GetMoodFilterListUseCase;
import org.sopt.snappinserver.global.response.code.mood.MoodSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/moods")
@RequiredArgsConstructor
@RestController
public class MoodControllerV2 implements MoodApi {

    private final GetMoodFilterListUseCase getMoodFilterListUseCase;

    @Override
    public ApiResponseBody<GetMoodFilterListResponse, Void> getAllMoodFilters() {
        GetMoodFilterListResult result = getMoodFilterListUseCase.getMoodFilters(null);
        GetMoodFilterListResponse response = GetMoodFilterListResponse.from(result);

        return ApiResponseBody.ok(MoodSuccessCode.GET_ALL_MOOD_OK, response);
    }
}
