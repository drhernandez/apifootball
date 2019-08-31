package com.santex.clients.imp;

import com.google.inject.Inject;
import com.netflix.config.ConfigurationManager;
import com.santex.clients.FootballApiClient;
import com.santex.models.entities.Team;
import com.santex.models.http.CompAndTeamsResp;
import kong.unirest.HttpMethod;
import kong.unirest.UnirestInstance;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

import static com.santex.configs.Constants.Headers.FOOTBAL_ORG_TOKEN;
import static com.santex.configs.Constants.Headers.X_AUTH_TOKEN;

@Slf4j
public class FootballApiClientImp implements FootballApiClient {

    public static final String CLIENT_NAME = "football-api";
    private static final String BASE_URL = ConfigurationManager.getConfigInstance().getString("restClient." + CLIENT_NAME + ".baseUrl");

    private final String token;
    private final UnirestInstance instance;

    @Inject
    public FootballApiClientImp(@Named(FOOTBAL_ORG_TOKEN) String token, @Named(CLIENT_NAME) UnirestInstance instance) {
        this.token = token;
        this.instance = instance;
    }

    @Override
    public CompAndTeamsResp getCompetitionTeams(String competitionCode) {

        String url = BASE_URL + "/competitions/{code}/teams";

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put(X_AUTH_TOKEN, token);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", competitionCode);

        return (CompAndTeamsResp) Client.builder()
                .restClient(instance)
                .url(url)
                .headers(headers)
                .parameters(parameters)
                .method(HttpMethod.GET)
                .responseMapperClass(CompAndTeamsResp.class)
                .build().executeRequest();
    }

    @Override
    public Team getTeam(Long teamId) {

        String url = BASE_URL + "/teams/{id}";

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put(X_AUTH_TOKEN, token);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", teamId.toString());

        return (Team) Client.builder()
                .restClient(instance)
                .url(url)
                .headers(headers)
                .parameters(parameters)
                .method(HttpMethod.GET)
                .responseMapperClass(Team.class)
                .build().executeRequest();
    }
}
