package org.sopt.snappinserver.api.v1.mood.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.mood.dto.response.GetMoodFilterListResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.mood.service.dto.response.GetMoodFilterListResult;
import org.sopt.snappinserver.domain.mood.service.usecase.GetMoodFilterListUseCase;
import org.sopt.snappinserver.global.response.code.mood.MoodSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/moods")
@RequiredArgsConstructor
@RestController
public class MoodController implements MoodApi {

    private final GetMoodFilterListUseCase getMoodFilterListUseCase;

    @Override
    public ApiResponseBody<GetMoodFilterListResponse, Void> getAllMoodFilters(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        Long userId = (userInfo != null) ? userInfo.userId() : null;
        GetMoodFilterListResult result = getMoodFilterListUseCase.getMoodFilters(userId);
        GetMoodFilterListResponse response = GetMoodFilterListResponse.from(result);

        return ApiResponseBody.ok(MoodSuccessCode.GET_ALL_MOOD_OK, response);
    }
}
