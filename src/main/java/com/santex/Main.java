package com.santex;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.santex.application.Application;
import com.santex.configs.Config;
import lombok.extern.slf4j.Slf4j;

import static com.santex.configs.Config.APP;

@Slf4j
public class Main extends Application {

    public Main() {
        super();
        Injector injector = Guice.createInjector();
        Config.addInjector(APP, injector);
    }

    public static void main(String[] args) {
        new Main().init();
        logger.info("Environment: {}", getEnvironment());
    }

    @Override
    public void addRoutes() {

    }
}
