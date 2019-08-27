package com.santex.configs;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.santex.application.Application;
import com.santex.clients.FootballApiClient;
import com.santex.clients.imp.FootballApiClientImp;

import javax.inject.Named;

public class InjectionConfigs extends AbstractModule {

    @Override
    protected void configure() {
        bind(FootballApiClient.class).to(FootballApiClientImp.class);
    }

    @Provides
    @Named(Constants.Headers.FOOTBAL_ORG_TOKEN)
    public String getApiKey() {
        return Application.getenv(Constants.Headers.FOOTBAL_ORG_TOKEN, "");
    }
}
