package org.sopt.snappinserver.api.photo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.snappinserver.api.photo.dto.request.PhotoProcessRequest;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "06 - Photo", description = "사진 관련 API")
public interface PhotoApi {

    @Operation(
        summary = "사진 <-> 무드 태그 연결",
        description = "람다에서 벡터로 변환된 사진과 무드 태그를 연결합니다. 웹에서 연결하는 API가 아닙니다! 람다 전용 API입니다."
    )
    ApiResponseBody<Void, Void> processPhoto(
        @RequestBody PhotoProcessRequest photoProcessRequest
    );
}
