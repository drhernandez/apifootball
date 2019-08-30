package com.santex.clients.imp;

import com.google.inject.Inject;
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
    private static final String BASE_URL = "http://api.football-data.org/v2";

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


//        HttpResponse<String> response;
//        try {
//            String url = BASE_URL + "/competitions/{code}/teams";
//            response = instance.get(url)
//                    .header("accept", "application/json")
//                    .header(X_AUTH_TOKEN, token)
//                    .routeParam("code", competitionCode)
//                    .asString();
//
//        } catch (Exception e) {
//            logger.error("[message: Unexpected error executing request] [request: GET /competitions/{}/teams] [error: {}]", competitionCode, ExceptionUtils.getLogMessage(e));
//            throw new ServerErrorException(e);
//        }
//
//        if (response.getStatus() != 200) {
//            logger.error("[message: Invalid status response] [request: GET /competitions/{}/teams] [status: {}] [error: {}]", competitionCode, response.getStatus(), response.getBody());
//            ErrorResponse errorResponse = MapperUtils.toObject(response.getBody(), ErrorResponse.class);
//            throw response.getStatus() == 404 ? new NotFoundException() : new ApiException(errorResponse.getMessage(), response.getStatus());
//        }
//
//        CompAndTeamsResp compAndTeamsResp;
//        try {
//            compAndTeamsResp = MapperUtils.toObject(response.getBody(), CompAndTeamsResp.class);
//        } catch (Exception e) {
//            logger.error("[message: Error trying to unmarshal response for get competition and teams request, code: {}] [error: {}] [body: {}]", competitionCode, ExceptionUtils.getLogMessage(e), response.getBody());
//            throw new ApiException("Invalid api response", HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
//        }
//
//        return compAndTeamsResp;
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

//        HttpResponse<String> response;
//        try {
//            String url = BASE_URL + "/teams/{id}";
//            response = instance.get(url)
//                    .header("accept", "application/json")
//                    .header(X_AUTH_TOKEN, token)
//                    .routeParam("id", teamId.toString())
//                    .asString();
//
//        } catch (Exception e) {
//            logger.error("[message: Unexpected error executing request] [request: GET /teams/{id}] [error: {}]", teamId, ExceptionUtils.getLogMessage(e));
//            throw new ServerErrorException(e);
//        }
//
//        if (response.getParsingError().isPresent()) {
//            UnirestParsingException e = response.getParsingError().get();
//            logger.error("[message: Error trying to unmarshal response] [error: {}] [body: {}]", e.getMessage(), e.getOriginalBody());
//            throw new ApiException("Unmarshal error", HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
//        }
//
//        if (response.getStatus() != 200) {
//            logger.error("[message: Invalid status response] [request: GET /teams/{}] [status: {}] [error: {}]", teamId, response.getStatus(), response.getBody());
//            ErrorResponse errorResponse = MapperUtils.toObject(response.getBody(), ErrorResponse.class);
//            throw response.getStatus() == 404 ? new NotFoundException() : new ApiException(errorResponse.getMessage(), response.getStatus());
//        }
//
//        Team team;
//        try {
//            team = MapperUtils.toObject(response.getBody(), Team.class);
//        } catch (Exception e) {
//            logger.error("[message: Error trying to unmarshal response for get team request, team: {}] [error: {}] [body: {}]", teamId, ExceptionUtils.getLogMessage(e), response.getBody());
//            throw new ApiException("Invalid api response", HttpStatus.SC_INTERNAL_SERVER_ERROR, e);
//        }
//
//        return team;
    }
}
