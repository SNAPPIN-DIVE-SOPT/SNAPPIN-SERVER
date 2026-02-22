package org.sopt.snappinserver.api.v1.curation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.v1.curation.dto.request.CreateMoodCurationRequest;
import org.sopt.snappinserver.api.v1.curation.dto.response.CreateMoodCurationResponse;
import org.sopt.snappinserver.api.v1.curation.dto.response.GetAllCurationQuestionsResponse;
import org.sopt.snappinserver.api.v1.curation.dto.response.GetCurationQuestionPhotosResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "03 - Curation", description = "무드 큐레이션 관련 API")
@Validated
public interface CurationApi {

    @Operation(
        summary = "큐레이션 단계별 질문/사진 조회 API",
        description = "로그인한 사용자가 큐레이션 별로 질문 / 사진을 조회할 수 있도록 합니다."
    )
    @GetMapping
    ApiResponseBody<GetCurationQuestionPhotosResponse, Void> getCurationQuestion(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "조회할 단계", example = "1")
        @RequestParam
        Integer step
    );

    @Operation(
        summary = "무드 큐레이션 결과 저장 및 결과 반환 API",
        description = "사용자가 선택한 사진에 대해 무드 큐레이션 결과를 저장하고, 결과를 반환합니다."
    )
    @PostMapping
    ApiResponseBody<CreateMoodCurationResponse, Void> createMoodCuration(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Valid
        @RequestBody
        CreateMoodCurationRequest request
    );

    @Operation(
        summary = "큐레이션 전체 질문/사진 조회",
        description = "로그인한 사용자가 전체 큐레이션 질문과 각 질문 별 사진을 한꺼번에 조회할 수 있습니다."
    )
    @GetMapping("/all")
    ApiResponseBody<GetAllCurationQuestionsResponse, Void> getAllCurationQuestions(
        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );
}
