package com.santex.exceptions;

import org.apache.http.HttpStatus;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(message, HttpStatus.SC_BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, HttpStatus.SC_BAD_REQUEST, cause);
    }
}
