package com.santex.exceptions;

import com.santex.utils.MapperUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.jetty.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String error;
    private String message;
    private Integer status = HttpStatus.INTERNAL_SERVER_ERROR_500;
    private List<String> causes;

    public ApiException(String error, String message) {
        super(message);
        this.error = error;
        this.message = message;
    }

    public ApiException(String error, String message, Integer status) {
        super(message);
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public ApiException(String error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
        this.message = message;
    }

    public ApiException(String error, String message, Integer status, Throwable cause) {
        super(message, cause);
        this.error = error;
        this.message = message;
        this.status = status;
    }

    public ApiException(String error, String message, Integer status, List<String> causes) {
        super(message);
        this.error = error;
        this.message = message;
        this.status = status;
        this.causes = causes;
    }

    public String toJson() {
        Map<String, Object> exceptionMap = new LinkedHashMap<>();

        exceptionMap.put("error", error);
        exceptionMap.put("message", message);
        exceptionMap.put("status", status);
        exceptionMap.put("cause", causes);

        try {
            return MapperUtils.toJsonString(exceptionMap);
        } catch (Exception exception) {
            return "{" + "\"error\"" + error + "\"message\"" +
                    message + "\"status\"" + status + "}";
        }
    }
}