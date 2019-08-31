package com.santex.routers;

import com.google.inject.Inject;
import com.santex.controllers.CompetitionsController;
import lombok.extern.slf4j.Slf4j;
import spark.RouteGroup;
import spark.Spark;

@Slf4j
public class CompetitionsRouter implements RouteGroup {

    CompetitionsController controller;

    @Inject
    public CompetitionsRouter(CompetitionsController controller) {
        this.controller = controller;
    }

    @Override
    public void addRoutes() {

        logger.info("Loading competitions routes...");
        Spark.path("import-league", () -> {

            Spark.get("/:code", controller::importLeague);
        });
    }
}
