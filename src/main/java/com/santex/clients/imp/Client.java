package com.santex.clients.imp;

import com.santex.exceptions.ApiException;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.exceptions.NotFoundException;
import com.santex.utils.MapperUtils;
import kong.unirest.HttpMethod;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestInstance;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Builder
public class Client<T> {

    final UnirestInstance restClient;
    final String url;
    final Map<String, String> headers;
    final String body;
    final Map<String, Object> parameters;
    final T responseMapperClass;
    final HttpMethod method;

    public T executeRequest() {

        HttpResponse<String> response;
        try {

            HttpRequestWithBody requestBuilder = restClient.request(method.name(), url);
            requestBuilder.headers(headers);
            requestBuilder.routeParam(parameters);
            if (body != null) {
                requestBuilder.body(body);
            }
            response = requestBuilder.asString();

//        } catch (SocketTimeoutException e) {
//            logger.error("[message: Connection error executing request] [request: {} {}] [error: {}]", method, url, ExceptionUtils.getLogMessage(e));
//            throw new ServerErrorException(e);
        } catch (Exception e) {
            logger.error("[message: Unexpected error executing request] [request: {} {} {}] [error: {}]", method, url, Arrays.toString(parameters.values().toArray()), ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException(e);
        }

        if (response.getStatus() != 200) {
            logger.error("[message: Invalid status response] [request: {} {} {}] [status: {}] [error: {}]", method, url, Arrays.toString(parameters.values().toArray()), response.getStatus(), response.getBody());
            throw response.getStatus() == 404 ? new NotFoundException() : new ApiException(response.getBody(), response.getStatus());
        }

        T object;
        try {

             object = MapperUtils.toObject(response.getBody(), (Class<T>) responseMapperClass);

        } catch (Exception e) {
            logger.error("[message: Error trying to unmarshal response] [request: {} {} {}] [body: {}] [error: {}]", method, url, Arrays.toString(parameters.values().toArray()), response.getBody(), ExceptionUtils.getLogMessage(e));
            throw new ApiException("Invalid api response", HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
        }

        return object;
    }
}
