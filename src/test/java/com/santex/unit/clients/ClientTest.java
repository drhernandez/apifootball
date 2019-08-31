package com.santex.unit.clients;

import com.santex.clients.imp.Client;
import com.santex.exceptions.ApiException;
import com.santex.exceptions.NotFoundException;
import com.santex.exceptions.ServerErrorException;
import com.santex.models.entities.Team;
import kong.unirest.*;
import org.junit.Test;

import java.net.SocketTimeoutException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {

    UnirestInstance instance = mock(UnirestInstance.class);

    @Test(expected = ServerErrorException.class)
    public void requestThrowException() {
        when(instance.request(anyString(), anyString())).thenThrow(new UnirestException(new SocketTimeoutException()));

        Client.builder()
                .restClient(instance)
                .url("")
                .method(HttpMethod.GET)
                .build().executeRequest();
    }

    @Test(expected = NotFoundException.class)
    public void errorNotFound() {
        HttpRequestWithBody requestWithBodyMock = mock(HttpRequestWithBody.class);
        HttpResponse<String> mock = mock(HttpResponse.class);

        when(instance.request(anyString(), anyString())).thenReturn(requestWithBodyMock);
        when(requestWithBodyMock.asString()).thenReturn(mock);
        when(mock.getStatus()).thenReturn(404);
        when(mock.getBody()).thenReturn("not_found");

        Client.builder()
                .restClient(instance)
                .url("")
                .method(HttpMethod.GET)
                .build().executeRequest();
    }

    @Test(expected = ApiException.class)
    public void unmarshalError() {

        HttpRequestWithBody requestWithBodyMock = mock(HttpRequestWithBody.class);
        HttpResponse<String> mock = mock(HttpResponse.class);

        when(instance.request(anyString(), anyString())).thenReturn(requestWithBodyMock);
        when(requestWithBodyMock.asString()).thenReturn(mock);
        when(mock.getStatus()).thenReturn(200);
        when(mock.getBody()).thenReturn("test");

        Client.builder()
                .restClient(instance)
                .url("")
                .method(HttpMethod.GET)
                .responseMapperClass(Team.class)
                .build().executeRequest();
    }
}
