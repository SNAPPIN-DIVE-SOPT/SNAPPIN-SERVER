package org.sopt.snappinserver.api.curation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.curation.code.CurationSuccessCode;
import org.sopt.snappinserver.api.curation.dto.request.CreateMoodCurationRequest;
import org.sopt.snappinserver.api.curation.dto.response.CreateMoodCurationResponse;
import org.sopt.snappinserver.api.curation.dto.response.GetCurationQuestionPhotosResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.curation.service.dto.request.CreateMoodCurationCommand;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodCurationResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.usecase.CreateMoodCurationUseCase;
import org.sopt.snappinserver.domain.curation.service.usecase.GetCurationQuestionUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/curation")
@RequiredArgsConstructor
@RestController
public class CurationController implements CurationApi {

    private final GetCurationQuestionUseCase getCurationQuestionUseCase;
    private final CreateMoodCurationUseCase createMoodCurationUseCase;

    @Override
    @GetMapping
    public ApiResponseBody<GetCurationQuestionPhotosResponse, Void> getCurationQuestion(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Integer step
    ) {
        GetCurationQuestionResult result = getCurationQuestionUseCase.retrieveCurationQuestionPhotos(
            userInfo.userId(),
            step
        );
        GetCurationQuestionPhotosResponse response = GetCurationQuestionPhotosResponse.from(result);

        return ApiResponseBody.ok(CurationSuccessCode.GET_CURATION_QUESTION_SUCCESS, response);
    }

    @Override
    @PostMapping
    public ApiResponseBody<CreateMoodCurationResponse, Void> createMoodCuration(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        @Valid @RequestBody CreateMoodCurationRequest request
    ) {
        CreateMoodCurationCommand command = CreateMoodCurationCommand.of(
            userInfo.userId(),
            request
        );
        CreateMoodCurationResult result = createMoodCurationUseCase.saveMoodCurationResult(command);
        CreateMoodCurationResponse response = CreateMoodCurationResponse.from(result);

        return ApiResponseBody.ok(CurationSuccessCode.CREATE_MOOD_CURATION_SUCCESS, response);
    }
}
