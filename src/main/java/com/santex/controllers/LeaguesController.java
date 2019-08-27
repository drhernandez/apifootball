package com.santex.controllers;

import com.google.inject.Inject;
import com.santex.clients.FootballApiClient;
import com.santex.models.entities.Team;
import com.santex.models.http.CompAndTeamsResp;
import com.santex.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

@Slf4j
public class LeaguesController {

    final FootballApiClient client;

    @Inject
    public LeaguesController(FootballApiClient client) {
        this.client = client;
    }

    public Object importLeague(Request request, Response response) {

        String leagueCode = request.params("id");
//        CompAndTeamsResp resp = client.getCompetitionTeams(leagueCode);
        Team resp = client.getTeam(3L);
        response.header("Content-type", "application/json");
        response.body(MapperUtils.toJsonString(resp));

        return null;
    }
}
