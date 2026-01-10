package org.sopt.snappinserver.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.sopt.snappinserver.global.response.dto.ErrorMeta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleHttpMessageNotReadable(
        HttpMessageNotReadableException exception,
        HttpServletRequest request
    ) {
        log.error("HttpMessageNotReadableException: {}", exception.getMessage());

        ErrorMeta meta = new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseBody.onFailure(CommonErrorCode.INVALID_MAPPING_PARAMETER, meta));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleNoResourceFoundException(
        NoResourceFoundException exception,
        HttpServletRequest request
    ) {
        log.error("NoResourceFoundException: {}", exception.getMessage());

        ErrorMeta meta = new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponseBody.onFailure(CommonErrorCode.RESOURCE_NOT_FOUND, meta));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException exception,
        HttpServletRequest request
    ) {
        log.error("MissingServletRequestParameterException: {}", exception.getMessage());

        String message = exception.getParameterName() + "는 필수입니다.";

        ErrorMeta meta = new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseBody.onFailure(
                    CommonErrorCode.INVALID_MAPPING_PARAMETER,
                    message,
                    meta
                )
            );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleMissingServletRequestParameterException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        log.error("MethodArgumentNotValidException: {}", exception.getMessage());

        String message = Objects.requireNonNull(exception.getBindingResult()
                .getFieldError())
            .getDefaultMessage();

        ErrorMeta meta = new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseBody.onFailure(
                    CommonErrorCode.INVALID_MAPPING_PARAMETER,
                    message,
                    meta
                )
            );
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handleDateTimeParseException(
        DateTimeParseException exception,
        HttpServletRequest request
    ) {
        log.error("DateTimeParseException: {}", exception.getMessage());

        ErrorMeta meta = new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseBody.onFailure(CommonErrorCode.INVALID_MAPPING_PARAMETER, meta));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handle(
        BusinessException exception,
        HttpServletRequest request
    ) {
        log.error("[errorCode={}] {}", exception.getErrorCode().getCode(), exception.getMessage());

        ErrorMeta meta = new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );

        return ResponseEntity
            .status(exception.getErrorCode().getStatus())
            .body(ApiResponseBody.onFailure(exception.getErrorCode(), meta));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseBody<Void, ErrorMeta>> handle(
        Exception exception,
        HttpServletRequest request
    ) {
        log.error("{}: {}", exception.getClass().getName(), exception.getMessage());

        ErrorMeta meta = new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponseBody.onFailure(
                    CommonErrorCode.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    meta
                )
            );
    }
}
