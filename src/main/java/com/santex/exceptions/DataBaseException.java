package com.santex.exceptions;

public class DataBaseException extends ApiException {

    public DataBaseException(String message) {
        super(message);
    }

    public DataBaseException(String message, Integer status) {
        super(message, status);
    }

    public DataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBaseException(String message, Integer status, Throwable cause) {
        super(message, status, cause);
    }
}
