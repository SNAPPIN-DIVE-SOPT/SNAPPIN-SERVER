package org.sopt.snappinserver.api.curation.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.curation.code.CurationSuccessCode;
import org.sopt.snappinserver.api.curation.dto.response.GetCurationQuestionPhotosResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationErrorCode;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationException;
import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;
import org.sopt.snappinserver.domain.curation.service.usecase.GetCurationQuestionUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/curation")
@RequiredArgsConstructor
@RestController
public class CurationController implements CurationApi {

    private final GetCurationQuestionUseCase getCurationQuestionUseCase;

    @Override
    @GetMapping
    public ApiResponseBody<GetCurationQuestionPhotosResponse, Void> getCurationQuestion(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        @NotNull(message = "단계는 필수입니다.") @RequestParam Integer step
    ) {
        validateLoginUser(userInfo);
        GetCurationQuestionResult result = getCurationQuestionUseCase.getCurationQuestionPhotos(
            userInfo.userId(),
            step
        );
        GetCurationQuestionPhotosResponse response = GetCurationQuestionPhotosResponse.from(result);

        return ApiResponseBody.ok(CurationSuccessCode.GET_CURATION_QUESTION_SUCCESS, response);
    }

    private void validateLoginUser(CustomUserInfo userInfo) {
        if(userInfo == null) {
            throw new CurationException(CurationErrorCode.CURATION_LOGIN_REQUIRED);
        }
    }
}
