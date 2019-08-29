package com.santex.controllers;

import com.google.inject.Inject;
import com.santex.models.entities.Competition;
import com.santex.services.CompetitionsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import spark.Request;
import spark.Response;

@Slf4j
public class CompetitionsController {

    private final CompetitionsService competitionsService;

    @Inject
    public CompetitionsController(CompetitionsService competitionsService) {
        this.competitionsService = competitionsService;
    }

    public Object importLeague(Request request, Response response) {

        String competitionCode = request.params("code");
        Competition competition = competitionsService.importLeague(competitionCode);


        response.header("Content-type", "application/json");
        if (competition.isFullyImported()) {
            response.status(HttpStatus.SC_CREATED);
            response.body("{\"message\": \"Successfully imported\"}");
        } else {
            response.status(HttpStatus.SC_PARTIAL_CONTENT);
            response.body("{\"message\": \"Partially imported\"}");
        }

        return null;
    }
}
