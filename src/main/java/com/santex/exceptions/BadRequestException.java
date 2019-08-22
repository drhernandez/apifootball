package com.santex.exceptions;

import com.santex.enums.ErrorCodes;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(ErrorCodes.bad_request.name(), message, HttpStatus.BAD_REQUEST_400);
    }

    public BadRequestException(String message, Throwable cause) {
        super(ErrorCodes.bad_request.name(), message, HttpStatus.BAD_REQUEST_400, cause);
    }

    public BadRequestException(String message, List<String> causes) {
        super(ErrorCodes.bad_request.name(), message, HttpStatus.BAD_REQUEST_400, causes);
    }
}
