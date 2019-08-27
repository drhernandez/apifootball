package com.santex.routers;

import com.google.common.net.MediaType;
import com.santex.enums.ErrorCodes;
import com.santex.exceptions.ApiException;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ErrorHandlerRouter {

    public void register() {
        Spark.notFound(notFound);
        Spark.exception(ApiException.class, this::apiExceptionHandler);
        Spark.exception(Exception.class, this::exceptionHandler);
    }

    Route notFound = (Request request, Response response) -> {
        ApiException apiException = new NotFoundException(String.format("Route %s not found", request.url()));
        logger.error(ExceptionUtils.getLogMessage(apiException));

        response.status(HttpStatus.NOT_FOUND_404);
        setHeaders(response);
        return apiException.toJson();
    };

    protected void apiExceptionHandler(ApiException exception, Request request, Response response) {
        response.status(exception.getStatus());
        response.body(exception.toJson());
        setHeaders(response);
    }

    protected void exceptionHandler(Exception exception, Request request, Response response) {
        Throwable t = ExceptionUtils.getFromChain(exception, ApiException.class);
        logger.error(ExceptionUtils.getLogMessage(t));
        ApiException apiException = t instanceof ApiException ? (ApiException) t : new InternalErrorException();

        response.status(apiException.getStatus());
        response.body(apiException.toJson());
        setHeaders(response);
    }

    private void setHeaders(Response response) {
        if (!response.raw().containsHeader("Content-Type")) {
            response.header("Content-Type", MediaType.JSON_UTF_8.toString());
        }

        response.header("Vary", "Accept,Accept-Encoding");
        response.header("Cache-Control", "max-age=0");
    }
}
