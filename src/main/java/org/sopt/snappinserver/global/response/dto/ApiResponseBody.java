package org.sopt.snappinserver.global.response.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "공통 응답 DTO")
public record ApiResponseBody<T, M>(

    @Schema(description = "해당 API의 성공 여부를 반환합니다. true면 성공, false면 실패입니다.")
    boolean success,

    @Schema(description = "해당 API의 HTTP 상태 코드입니다.")
    int status,

    @Schema(description = "해당 API의 결과에 대한 상태 메시지입니다.")
    String message,

    @Schema(description = "해당 API 관련 커스텀 코드입니다. 도메인(3글자)-상태코드-순번 으로 이루어져 있습니다.", example = "TIC_200_001")
    String code,

    @Schema(description = "해당 API에서 반환하는 결과 데이터입니다.")
    T data,

    @Schema(description = "해당 API의 data를 설명하는 meta data입니다. 페이지네이션 정보나, 에러 발생 시 에러 정보를 반환합니다.")
    M meta
) {

    public static <T> ApiResponseBody<T, Void> ok(SuccessCode successCode, T data) {
        return new ApiResponseBody<>(
            true,
            successCode.getStatus(),
            successCode.getMessage(),
            successCode.getCode(),
            data,
            null
        );
    }

    public static <T, M> ApiResponseBody<T, M> ok(SuccessCode successCode, T data, M meta) {
        return new ApiResponseBody<>(
            true,
            successCode.getStatus(),
            successCode.getMessage(),
            successCode.getCode(),
            data,
            meta
        );
    }

    public static ApiResponseBody<Void, ErrorMeta> onFailure(
        ErrorCode errorCode,
        ErrorMeta errorMeta
    ) {
        return new ApiResponseBody<>(
            false,
            errorCode.getStatus(),
            errorCode.getMessage(),
            errorCode.getCode(),
            null,
            errorMeta
        );
    }

    public static ApiResponseBody<Void, ErrorMeta> onFailure(
        ErrorCode errorCode,
        String message,
        ErrorMeta errorMeta
    ) {
        return new ApiResponseBody<>(
            false,
            errorCode.getStatus(),
            message,
            errorCode.getCode(),
            null,
            errorMeta
        );
    }
}
