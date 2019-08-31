package com.santex.exceptions;

import org.apache.http.HttpStatus;

public class ServerErrorException extends ApiException {

    public ServerErrorException() {
        super("Server error", HttpStatus.SC_GATEWAY_TIMEOUT);
    }

    public ServerErrorException(String message) {
        super(message, HttpStatus.SC_GATEWAY_TIMEOUT);
    }

    public ServerErrorException(Throwable cause) {
        super("Server error", HttpStatus.SC_GATEWAY_TIMEOUT, cause);
    }
}
