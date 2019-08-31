package com.santex.controllers;

import com.google.inject.Inject;
import com.santex.exceptions.BadRequestException;
import com.santex.services.PlayersService;
import org.apache.http.HttpStatus;
import spark.Request;
import spark.Response;

public class PlayersController {

    private final PlayersService playersService;

    @Inject
    public PlayersController(PlayersService playersService) {
        this.playersService = playersService;
    }

    public Object countPlayers(Request request, Response response) {

        String competitionCode = request.params("code");
        if (competitionCode == null) {
            throw new BadRequestException("Invalid params");
        }

        long count = playersService.countPlayersByCompetition(competitionCode);

        response.header("Content-type", "application/json");
        response.status(HttpStatus.SC_OK);
        response.body(String.format("{\"total\": %d}", count));

        return null;
    }
}
