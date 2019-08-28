package com.santex.exceptions;

import com.santex.enums.ErrorCodes;
import org.apache.http.HttpStatus;

public class ServerErrorException extends ApiException {

    public ServerErrorException() {
        super(ErrorCodes.server_error.name(), "Server error", HttpStatus.SC_GATEWAY_TIMEOUT);
    }

    public ServerErrorException(Throwable cause) {
        super(ErrorCodes.server_error.name(), "Server error", HttpStatus.SC_GATEWAY_TIMEOUT, cause);
    }
}
