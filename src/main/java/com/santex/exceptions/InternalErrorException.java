package com.santex.exceptions;

import com.santex.enums.ErrorCodes;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;

public class InternalErrorException extends ApiException {

    public InternalErrorException(String message) {
        super(ErrorCodes.internal_error.name(), message, HttpStatus.INTERNAL_SERVER_ERROR_500);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(ErrorCodes.internal_error.name(), message, HttpStatus.INTERNAL_SERVER_ERROR_500, cause);
    }

    public InternalErrorException(String message, List<String> causes) {
        super(ErrorCodes.internal_error.name(), message, HttpStatus.INTERNAL_SERVER_ERROR_500, causes);
    }
}
