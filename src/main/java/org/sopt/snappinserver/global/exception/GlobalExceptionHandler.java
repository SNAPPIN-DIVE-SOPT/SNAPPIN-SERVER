package org.sopt.snappinserver.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.sopt.snappinserver.global.response.dto.ErrorMeta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =========================
     * 1. RequestBody 파싱 실패
     * ========================= */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleHttpMessageNotReadable(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ) {
        log.error("HttpMessageNotReadableException: {}", exception.getMessage());

        return badRequest(
            CommonErrorCode.INVALID_MAPPING_PARAMETER,
            request
        );
    }

    /* =========================
     * 2. RequestParam 바인딩 오류
     *  - step 없음
     *  - step=
     *  - step=abc
     * ========================= */
    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleRequestParamBindingException(
        Exception exception,
        HttpServletRequest request
    ) {
        log.error("RequestParam binding error: {}", exception.getMessage());

        return badRequest(
            CommonErrorCode.INVALID_REQUEST_VARIABLE,
            "단계는 필수입니다.",
            request
        );
    }

    /* =========================
     * 3. RequestParam / PathVariable 검증 실패
     *  (@NotNull, @Min, @Max 등)
     * ========================= */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleConstraintViolation(
        ConstraintViolationException exception,
        HttpServletRequest request
    ) {
        log.error("ConstraintViolationException: {}", exception.getMessage());

        String message = exception.getConstraintViolations().stream()
            .findFirst()
            .map(ConstraintViolation::getMessage)
            .orElse("잘못된 요청입니다.");

        return badRequest(
            CommonErrorCode.INVALID_REQUEST_VARIABLE,
            message,
            request
        );
    }

    /* =========================
     * 4. RequestBody @Valid 검증 실패
     * ========================= */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        log.error("MethodArgumentNotValidException: {}", exception.getMessage());

        String message = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(FieldError::getDefaultMessage)
            .orElse("잘못된 요청입니다.");

        return badRequest(
            CommonErrorCode.INVALID_MAPPING_PARAMETER,
            message,
            request
        );
    }

    /* =========================
     * 5. 날짜 파싱 실패
     * ========================= */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleDateTimeParseException(
        DateTimeParseException exception,
        HttpServletRequest request
    ) {
        log.error("DateTimeParseException: {}", exception.getMessage());

        return badRequest(
            CommonErrorCode.INVALID_MAPPING_PARAMETER,
            request
        );
    }

    /* =========================
     * 6. 존재하지 않는 엔드포인트
     * ========================= */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleNoResourceFound(
        NoResourceFoundException exception,
        HttpServletRequest request
    ) {
        log.error("NoResourceFoundException: {}", exception.getMessage());

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponseBody.onFailure(
                CommonErrorCode.RESOURCE_NOT_FOUND,
                meta(request)
            ));
    }

    /* =========================
     * 7. 도메인 / 비즈니스 예외
     * ========================= */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleBusinessException(
        BusinessException exception,
        HttpServletRequest request
    ) {
        log.error("[{}] {}", exception.getErrorCode().getCode(), exception.getMessage());

        return ResponseEntity
            .status(exception.getErrorCode().getStatus())
            .body(ApiResponseBody.onFailure(
                exception.getErrorCode(),
                meta(request)
            ));
    }

    /* =========================
     * 8. 1~7에서 잡지 못한 예외 (어떤 예외가 발생했는지 실제 발생 예외를 message에 던짐)
     * ========================= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleUnexpected(
        Exception exception,
        HttpServletRequest request
    ) {
        log.error("Unhandled exception", exception);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseBody.onFailure(
                CommonErrorCode.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                meta(request)
            ));
    }

    private ResponseEntity<ApiResponseBody<Void, ErrorMeta>> badRequest(
        CommonErrorCode errorCode,
        HttpServletRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseBody.onFailure(
                errorCode,
                meta(request)
            ));
    }

    private ResponseEntity<ApiResponseBody<Void, ErrorMeta>> badRequest(
        CommonErrorCode errorCode,
        String message,
        HttpServletRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseBody.onFailure(
                errorCode,
                message,
                meta(request)
            ));
    }

    private ErrorMeta meta(HttpServletRequest request) {
        return new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );
    }
}
