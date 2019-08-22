package com.santex.exceptions;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ClientException extends ApiException {

    public ClientException(String error, String message) {
        super(error, message);
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
