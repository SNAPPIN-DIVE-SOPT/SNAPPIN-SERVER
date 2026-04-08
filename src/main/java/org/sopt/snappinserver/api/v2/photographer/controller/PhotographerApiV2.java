package org.sopt.snappinserver.api.v2.photographer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.sopt.snappinserver.api.v2.photographer.dto.response.GetPhotographerProfileResponseV2;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "02 - Photographer", description = "스냅 작가 관련 API")
@Validated
public interface PhotographerApiV2 {

    @Operation(
        summary = "작가 상세 조회 API V2",
        description = "입력받은 photographerId로 해당 작가의 프로필을 조회합니다. (contactLink 포함)"
    )
    @GetMapping("/{photographerId}")
    ApiResponseBody<GetPhotographerProfileResponseV2, Void> getPhotographerProfile(
        @Parameter(description = "조회할 작가 ID")
        @NotNull(message = "작가 ID는 필수입니다.")
        @Positive(message = "작가 ID는 양수여야 합니다.")
        @PathVariable
        Long photographerId
    );
}
