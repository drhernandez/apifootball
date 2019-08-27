package com.santex.exceptions;

import com.santex.enums.ErrorCodes;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;

public class ClientException extends ApiException {

    public ClientException(String message) {
        super(ErrorCodes.client_error.name(), message, HttpStatus.INTERNAL_SERVER_ERROR_500);
    }

    public ClientException(String error, String message, Integer status) {
        super(error, message, status);
    }

    public ClientException(String error, String message, Throwable cause) {
        super(error, message, cause);
    }

    public ClientException(String error, String message, Integer status, Throwable cause) {
        super(error, message, status, cause);
    }

    public ClientException(String error, String message, Integer status, List<String> causes) {
        super(error, message, status, causes);
    }
}
