package com.santex;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.santex.application.Application;
import com.santex.configs.*;
import lombok.extern.slf4j.Slf4j;
import spark.RouteGroup;

import java.util.List;

import static com.santex.configs.Injectors.APP;

@Slf4j
public class Main extends Application {

    public Main() {
        super();
        Injector injector = Guice.createInjector(
                new DataBaseConfigs(),
                new RoutingConfigs(),
                new RestClientConfigs(),
                new InjectionConfigs()
        );
        Injectors.addInjector(APP, injector);
    }

    public static void main(String[] args) {
        new Main().init();
        logger.info("Environment: {}", getEnvironment());
    }

    @Override
    public void addRoutes() {
        //Load all registered routers
        List<Binding<RouteGroup>> bindings = Injectors.getInjector(APP).findBindingsByType(TypeLiteral.get(RouteGroup.class));
        bindings.forEach(binding -> binding.getProvider().get().addRoutes());
    }

    @Override
    public void destroy() {
        logger.info("destroy");
        super.destroy();
        RestClientConfigs.shutDownClients();
    }
}
