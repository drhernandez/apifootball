package com.santex.controllers;

import com.google.inject.Inject;
import com.santex.models.entities.Team;
import com.santex.services.CompetitionsService;
import com.santex.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import spark.Request;
import spark.Response;

@Slf4j
public class LeaguesController {

    private final CompetitionsService competitionsService;

    @Inject
    public LeaguesController(CompetitionsService competitionsService) {
        this.competitionsService = competitionsService;
    }

    public Object importLeague(Request request, Response response) {

        String leagueCode = request.params("id");
        competitionsService.importLeague(leagueCode);
        response.header("Content-type", "application/json");
        response.status(HttpStatus.SC_CREATED);
        response.body("{\"message\": \"Successfully imported\"}");

        return null;
    }
}
