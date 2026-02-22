package org.sopt.snappinserver.api.v1.curation.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.curation.dto.request.CreateMoodCurationRequest;
import org.sopt.snappinserver.api.v1.curation.dto.response.CreateMoodCurationResponse;
import org.sopt.snappinserver.api.v1.curation.dto.response.GetAllCurationQuestionsResponse;
import org.sopt.snappinserver.api.v1.curation.dto.response.GetCurationQuestionPhotosResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.curation.service.dto.request.CreateMoodCurationCommand;
import org.sopt.snappinserver.domain.curation.service.dto.response.CreateMoodCurationResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetAllCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.usecase.CreateMoodCurationUseCase;
import org.sopt.snappinserver.domain.curation.service.usecase.GetAllCurationQuestionUseCase;
import org.sopt.snappinserver.domain.curation.service.usecase.GetCurationQuestionUseCase;
import org.sopt.snappinserver.global.response.code.curation.CurationSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/curation")
@RequiredArgsConstructor
@RestController
public class CurationController implements CurationApi {

    private final GetCurationQuestionUseCase getCurationQuestionUseCase;
    private final CreateMoodCurationUseCase createMoodCurationUseCase;
    private final GetAllCurationQuestionUseCase getAllCurationQuestionUseCase;

    @Override
    public ApiResponseBody<GetCurationQuestionPhotosResponse, Void> getCurationQuestion(
        @AuthenticationPrincipal CustomUserInfo userInfo,

        @NotNull(message = "단계는 필수입니다.")
        @Min(value = 1, message = "단계는 1 이상이어야 합니다.")
        @Max(value = 5, message = "단계는 5 이하여야 합니다.")
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
    public ApiResponseBody<CreateMoodCurationResponse, Void> createMoodCuration(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        CreateMoodCurationRequest request
    ) {
        CreateMoodCurationCommand command = CreateMoodCurationCommand.of(
            userInfo.userId(),
            request
        );
        CreateMoodCurationResult result = createMoodCurationUseCase.saveMoodCurationResult(command);
        CreateMoodCurationResponse response = CreateMoodCurationResponse.from(result);

        return ApiResponseBody.ok(CurationSuccessCode.CREATE_MOOD_CURATION_SUCCESS, response);
    }

    @Override
    public ApiResponseBody<GetAllCurationQuestionsResponse, Void> getAllCurationQuestions(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        GetAllCurationQuestionResult result = getAllCurationQuestionUseCase
            .getAllCurationQuestions(userInfo.userId());
        GetAllCurationQuestionsResponse response = GetAllCurationQuestionsResponse.from(result);

        return ApiResponseBody.ok(CurationSuccessCode.GET_CURATION_QUESTION_SUCCESS, response);
    }
}
