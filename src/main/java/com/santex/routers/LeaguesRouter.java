package com.santex.routers;

import com.google.inject.Inject;
import com.santex.controllers.LeaguesController;
import lombok.extern.slf4j.Slf4j;
import spark.RouteGroup;
import spark.Spark;

@Slf4j
public class LeaguesRouter implements RouteGroup {

    LeaguesController controller;

    @Inject
    public LeaguesRouter(LeaguesController controller) {
        this.controller = controller;
    }

    @Override
    public void addRoutes() {

        logger.info("Loading leagues routes...");
        Spark.path("import-league", () -> {

            Spark.get("/:id", controller::importLeague);
        });
    }
}
