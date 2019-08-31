package com.santex.unit.clients;

import com.santex.TestUtils;
import com.santex.clients.imp.FootballApiClientImp;
import com.santex.models.entities.Team;
import com.santex.models.http.CompAndTeamsResp;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestInstance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FootballApiClientTest {

    private UnirestInstance instance = mock(UnirestInstance.class);
    private FootballApiClientImp client = new FootballApiClientImp("token", instance);

    @Test
    public void getCompetitionTeamsOk() {

        HttpRequestWithBody requestWithBodyMock = mock(HttpRequestWithBody.class);
        HttpResponse<String> mock = mock(HttpResponse.class);

        when(instance.request(anyString(), anyString())).thenReturn(requestWithBodyMock);
        when(requestWithBodyMock.asString()).thenReturn(mock);
        when(mock.getStatus()).thenReturn(200);
        when(mock.getBody()).thenReturn(TestUtils.getJsonString("/mocks/compAndTeams", "getCompetitionAndTeams_ok.json"));

        CompAndTeamsResp response = client.getCompetitionTeams("PL");

        assertNotNull(response.getCompetition());
        assertNotNull(response.getTeams());
        assertEquals("PL", response.getCompetition().getCode());
        assertEquals(5, response.getTeams().size());
    }

    @Test
    public void getTeamsOk() {

        HttpRequestWithBody requestWithBodyMock = mock(HttpRequestWithBody.class);
        HttpResponse<String> mock = mock(HttpResponse.class);

        when(instance.request(anyString(), anyString())).thenReturn(requestWithBodyMock);
        when(requestWithBodyMock.asString()).thenReturn(mock);
        when(mock.getStatus()).thenReturn(200);
        when(mock.getBody()).thenReturn(TestUtils.getJsonString("/mocks/team", "getTeams_ok.json"));

        Team team = client.getTeam(98L);

        assertNotNull(team);
        assertEquals("AC Milan", team.getName());
    }
}
