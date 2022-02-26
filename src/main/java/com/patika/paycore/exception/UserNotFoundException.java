package com.patika.paycore.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseRuntimeException {

    public UserNotFoundException(int errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorCode, errorMessage, httpStatus);
    }
}
