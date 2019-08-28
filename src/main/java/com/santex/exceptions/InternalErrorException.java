package com.santex.exceptions;

import com.santex.enums.ErrorCodes;
import org.apache.http.HttpStatus;

import java.util.List;

public class InternalErrorException extends ApiException {

    public InternalErrorException() {
        super(ErrorCodes.internal_error.name(), "Internal error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    public InternalErrorException(Throwable t) {
        super(ErrorCodes.internal_error.name(), "Internal error", HttpStatus.SC_INTERNAL_SERVER_ERROR, t);
    }

    public InternalErrorException(String message) {
        super(ErrorCodes.internal_error.name(), message, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(ErrorCodes.internal_error.name(), message, HttpStatus.SC_INTERNAL_SERVER_ERROR, cause);
    }

    public InternalErrorException(String message, List<String> causes) {
        super(ErrorCodes.internal_error.name(), message, HttpStatus.SC_INTERNAL_SERVER_ERROR, causes);
    }
}
