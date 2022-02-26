package com.patika.paycore.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseRuntimeException {

    public UserAlreadyExistsException(int errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorCode, errorMessage, httpStatus);
    }
}
