package org.sopt.snappinserver.api.curation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.sopt.snappinserver.api.curation.dto.request.CreateMoodCurationRequest;
import org.sopt.snappinserver.api.curation.dto.response.CreateMoodCurationResponse;
import org.sopt.snappinserver.api.curation.dto.response.GetCurationQuestionPhotosResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "03 - Curation", description = "무드 큐레이션 관련 API")
@Validated
public interface CurationApi {

    @Operation(
        summary = "큐레이션 단계별 질문/사진 조회 API",
        description = "로그인한 사용자가 큐레이션 별로 질문 / 사진을 조회할 수 있도록 합니다."
    )
    ApiResponseBody<GetCurationQuestionPhotosResponse, Void> getCurationQuestion(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "조회할 단계", example = "1")
        @NotNull(message = "단계는 필수입니다.")
        @Min(value = 1, message = "단계는 1 이상이어야 합니다.")
        @Max(value = 5, message = "단계는 5 이하여야 합니다.")
        @RequestParam Integer step
    );

    @Operation(
        summary = "무드 큐레이션 결과 저장 및 결과 반환 API",
        description = "사용자가 선택한 사진에 대해 무드 큐레이션 결과를 저장하고, 결과를 반환합니다."
    )
    ApiResponseBody<CreateMoodCurationResponse, Void> createMoodCuration(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Valid @RequestBody CreateMoodCurationRequest request
    );
}
