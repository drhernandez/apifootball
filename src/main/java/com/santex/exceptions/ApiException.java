package com.santex.exceptions;

import com.santex.utils.MapperUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.jetty.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;
    private Integer status = HttpStatus.INTERNAL_SERVER_ERROR_500;

    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    public ApiException(String message, Integer status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ApiException(String message, Integer status, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.status = status;
    }

    public String toJson() {
        Map<String, Object> exceptionMap = new LinkedHashMap<>();

        exceptionMap.put("message", message);

        try {
            return MapperUtils.toJsonString(exceptionMap);
        } catch (Exception exception) {
            return "{" + "\"message\"" + message + "}";
        }
    }
}