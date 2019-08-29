package com.santex.routers;

import com.google.inject.Inject;
import com.santex.controllers.PlayersController;
import lombok.extern.slf4j.Slf4j;
import spark.RouteGroup;
import spark.Spark;

@Slf4j
public class PlayersRouter implements RouteGroup {

    PlayersController controller;

    @Inject
    public PlayersRouter(PlayersController controller) {
        this.controller = controller;
    }

    @Override
    public void addRoutes() {

        logger.info("Loading leagues routes...");
        Spark.path("total-players", () -> {

            Spark.get("/:code", controller::countPlayers);
        });
    }
}
