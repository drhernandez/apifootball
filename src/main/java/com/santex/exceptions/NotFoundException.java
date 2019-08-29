package com.santex.exceptions;

import org.apache.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException() {
        super("Not found", HttpStatus.SC_NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.SC_NOT_FOUND);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, HttpStatus.SC_NOT_FOUND, cause);
    }
}
