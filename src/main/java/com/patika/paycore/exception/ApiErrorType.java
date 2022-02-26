package com.patika.paycore.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiErrorType {
    INTERNAL_SERVER_ERROR(2000, "Internal Server Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    FIELD_VALIDATION_ERROR(2001, "Field Validation Exception", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND_ERROR(2002, "User Not Found Exception", HttpStatus.NOT_FOUND),
    AUTHENTICATION_ERROR(2003, "Authentication Exception", HttpStatus.UNAUTHORIZED),
    USER_EXISTS_ERROR(2004, "User Already Exists Exception", HttpStatus.NOT_ACCEPTABLE);


    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;


}
