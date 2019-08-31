package com.santex.clients.imp;

import com.santex.exceptions.ApiException;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.NotFoundException;
import com.santex.exceptions.ServerErrorException;
import com.santex.utils.MapperUtils;
import kong.unirest.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Client<T> {

    private UnirestInstance restClient;
    private String url;
    private Map<String, String> headers;
    private String body;
    private Map<String, Object> parameters;
    private T responseMapperClass;
    private HttpMethod method;

    @Builder
    public Client(UnirestInstance restClient, String url, Map<String, String> headers, String body, Map<String, Object> parameters, T responseMapperClass, HttpMethod method) {
        this.restClient = restClient;
        this.url = url;
        this.headers = headers != null ? headers : new HashMap<>();
        this.body = body;
        this.parameters = parameters != null ? parameters : new HashMap<>();
        this.responseMapperClass = responseMapperClass;
        this.method = method;
    }

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

        } catch (UnirestException e) {
            logger.error("[message: Connection error executing request] [request: {} {} {}] [error: {}]", method, url, Arrays.toString(parameters.values().toArray()), ExceptionUtils.getLogMessage(e));
            throw new ServerErrorException(e);
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
