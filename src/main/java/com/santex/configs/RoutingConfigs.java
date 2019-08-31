package com.santex.configs;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.santex.routers.CompetitionsRouter;
import com.santex.routers.PlayersRouter;
import spark.RouteGroup;

public class RoutingConfigs extends AbstractModule {

    @Override
    protected void configure() {

        Multibinder<RouteGroup> routeGroupMultibinder = Multibinder.newSetBinder(binder(), RouteGroup.class);
        routeGroupMultibinder.addBinding().to(CompetitionsRouter.class);
        routeGroupMultibinder.addBinding().to(PlayersRouter.class);
    }
}
