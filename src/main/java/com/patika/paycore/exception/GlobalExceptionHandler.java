package com.patika.paycore.exception;

import com.patika.paycore.model.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleBaseRuntimeException(BaseRuntimeException exception) {
        log.error("Api exception occurred. Exception: {}, errorCode: {}, errorMessage: {}",
                exception.getClass().getName(),
                exception.getErrorCode(),
                exception.getErrorMessage());
        return createErrorResponse(exception.getHttpStatus(), exception.getErrorMessage(), exception.getErrorCode());

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException exception) {
        log.error("Api exception occurred. Exception: {}, errorCode: {}, errorMessage: {}",
                exception.getClass().getName(),
                ApiErrorType.INTERNAL_SERVER_ERROR.getErrorCode(),
                ApiErrorType.INTERNAL_SERVER_ERROR.getErrorMessage());

        return createErrorResponse(ApiErrorType.INTERNAL_SERVER_ERROR.getHttpStatus(),
                ApiErrorType.INTERNAL_SERVER_ERROR.getErrorMessage(),
                ApiErrorType.INTERNAL_SERVER_ERROR.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Api exception occurred. Exception: {}, errorCode: {}, errorMessage: {}",
                exception.getClass().getName(),
                ApiErrorType.FIELD_VALIDATION_ERROR.getErrorCode(),
                ApiErrorType.FIELD_VALIDATION_ERROR.getErrorMessage());

        return createErrorResponse(ApiErrorType.FIELD_VALIDATION_ERROR.getHttpStatus(),
                exception.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList()).toString(),
                ApiErrorType.FIELD_VALIDATION_ERROR.getErrorCode());
    }


    private ResponseEntity<ApiErrorResponse> createErrorResponse(HttpStatus httpStatus, String errorMessage, int errorCode) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .code(String.valueOf(errorCode))
                .message(errorMessage)
                .status(httpStatus)
                .build();
        return ResponseEntity.
                status(httpStatus).
                body(apiErrorResponse);
    }
}
