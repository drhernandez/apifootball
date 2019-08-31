package com.santex.exceptions;

import org.apache.http.HttpStatus;

public class InternalErrorException extends ApiException {

    public InternalErrorException() {
        super("Internal error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    public InternalErrorException(Throwable t) {
        super("Internal error", HttpStatus.SC_INTERNAL_SERVER_ERROR, t);
    }

    public InternalErrorException(String message) {
        super(message, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, HttpStatus.SC_INTERNAL_SERVER_ERROR, cause);
    }
}
