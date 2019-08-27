package com.santex.clients.imp;

import com.google.inject.Inject;
import com.santex.clients.FootballApiClient;
import com.santex.configs.Constants;
import com.santex.enums.ErrorCodes;
import com.santex.exceptions.ClientException;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.exceptions.NotFoundException;
import com.santex.models.entities.Team;
import com.santex.models.http.CompAndTeamsResp;
import com.santex.models.http.ErrorResponse;
import com.santex.utils.MapperUtils;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestInstance;
import kong.unirest.UnirestParsingException;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

import static com.santex.configs.Constants.Headers.X_AUTH_TOKEN;

@Slf4j
public class FootballApiClientImp implements FootballApiClient {

    public static final String CLIENT_NAME = "football-api";
    private static final String BASE_URL = "http://api.football-data.org/v2";

    private final String token;
    private final UnirestInstance instance;

    @Inject
    public FootballApiClientImp(@Named(Constants.Headers.FOOTBAL_ORG_TOKEN) String token, @Named(CLIENT_NAME) UnirestInstance instance) {
        this.token = token;
        this.instance = instance;
    }

    @Override
    public CompAndTeamsResp getCompetitionTeams(String competitionId) {

        HttpResponse<String> response;
        try {
            String url = BASE_URL + "/competitions/{id}/teams";
            response = instance.get(url)
                    .header("accept", "application/json")
                    .header(X_AUTH_TOKEN, token)
                    .routeParam("id", competitionId)
                    .asString();

        } catch (Exception e) {
            logger.error("[message: Unexpected error executing request] [request: GET /competitions/{}/teams] [error: {}]", competitionId, ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException();
        }

        if (response.getParsingError().isPresent()) {
            UnirestParsingException e = response.getParsingError().get();
            logger.error("[message: Error trying to unmarshal response] [error: {}] [body: {}]", e.getMessage(), e.getOriginalBody());
            throw new ClientException("Unmarshal error");
        }

        if (response.getStatus() != 200) {
            logger.error("[message: Invalid status response] [request: GET /competitions/{}/teams] [status: {}] [error: {}]", competitionId, response.getStatus(), response.getBody());
            ErrorResponse errorResponse = MapperUtils.toObject(response.getBody(), ErrorResponse.class);
            throw response.getStatus() == 404 ? new NotFoundException() : new ClientException(ErrorCodes.internal_error.toString(), errorResponse.getMessage(), response.getStatus());
        }

        return MapperUtils.toObject(response.getBody(), CompAndTeamsResp.class);
    }

    @Override
    public Team getTeam(Long teamId) {

        HttpResponse<String> response;
        try {
            String url = BASE_URL + "/teams/{id}";
            response = instance.get(url)
                    .header("accept", "application/json")
                    .header(X_AUTH_TOKEN, token)
                    .routeParam("id", teamId.toString())
                    .asString();

        } catch (Exception e) {
            logger.error("[message: Unexpected error executing request] [request: GET /teams/{id}] [error: {}]", teamId, ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException();
        }

        if (response.getParsingError().isPresent()) {
            UnirestParsingException e = response.getParsingError().get();
            logger.error("[message: Error trying to unmarshal response] [error: {}] [body: {}]", e.getMessage(), e.getOriginalBody());
            throw new ClientException("Unmarshal error");
        }

        if (response.getStatus() != 200) {
            logger.error("[message: Invalid status response] [request: GET /teams/{}] [status: {}] [error: {}]", teamId, response.getStatus(), response.getBody());
            ErrorResponse errorResponse = MapperUtils.toObject(response.getBody(), ErrorResponse.class);
            throw response.getStatus() == 404 ? new NotFoundException() : new ClientException(ErrorCodes.internal_error.toString(), errorResponse.getMessage(), response.getStatus());
        }

        return MapperUtils.toObject(response.getBody(), Team.class);
    }
}