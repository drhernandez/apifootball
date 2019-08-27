package com.santex.exceptions;

import com.santex.enums.ErrorCodes;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;

public class NotFoundException extends ApiException {

    public NotFoundException() {
        super(ErrorCodes.not_found.name(), "Not found", HttpStatus.NOT_FOUND_404);
    }

    public NotFoundException(String message) {
        super(ErrorCodes.not_found.name(), message, HttpStatus.NOT_FOUND_404);
    }

    public NotFoundException(String message, Throwable cause) {
        super(ErrorCodes.not_found.name(), message, HttpStatus.NOT_FOUND_404, cause);
    }

    public NotFoundException(String message, List<String> causes) {
        super(ErrorCodes.not_found.name(), message, HttpStatus.NOT_FOUND_404, causes);
    }
}
