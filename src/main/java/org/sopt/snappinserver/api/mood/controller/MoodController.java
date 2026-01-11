package org.sopt.snappinserver.api.mood.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.mood.code.MoodSuccessCode;
import org.sopt.snappinserver.api.mood.dto.response.GetMoodFilterListResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterListResult;
import org.sopt.snappinserver.domain.mood.service.usecase.GetMoodFilterListUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/moods")
@RequiredArgsConstructor
@RestController
public class MoodController implements MoodApi {

    private final GetMoodFilterListUseCase getMoodFilterListUseCase;

    @Override
    @GetMapping
    public ApiResponseBody<GetMoodFilterListResponse, Void> getAllMoodFilters(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        GetMoodFilterListResult result = getMoodFilterListUseCase.getMoodFilters(userInfo.userId());
        GetMoodFilterListResponse response = GetMoodFilterListResponse.from(result);

        return ApiResponseBody.ok(MoodSuccessCode.GET_ALL_MOOD_TAGS_OK, response);
    }
}
