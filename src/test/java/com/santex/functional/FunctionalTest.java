package com.santex.functional;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.santex.application.Application;
import com.santex.configs.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import spark.RouteGroup;
import spark.Spark;

import java.util.List;

import static com.santex.configs.Injectors.APP;

public class FunctionalTest extends Application {

    public FunctionalTest() {
        super();
        Injector injector = Guice.createInjector(
                new DataBaseTestConfigs(),
                new RoutingConfigs(),
                new RestClientConfigs(),
                new InjectionConfigs()
        );
        Injectors.addInjector(APP, injector);
    }

    @BeforeClass
    public static void startApp() {
        new FunctionalTest().init();
    }

    @AfterClass
    public static void finishApp() {
        Injectors.clearInjectors();
        RestClientTestConfigs.shutDownClients();
        Spark.stop();
    }

    @Override
    public void addRoutes() {
        List<Binding<RouteGroup>> bindings = Injectors.getInjector(APP).findBindingsByType(TypeLiteral.get(RouteGroup.class));
        bindings.stream().forEach(binding -> binding.getProvider().get().addRoutes());
    }
}
