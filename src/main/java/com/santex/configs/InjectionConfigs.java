package com.santex.configs;

import com.google.inject.AbstractModule;
import com.santex.clients.LeaguesClient;
import com.santex.clients.imp.LeaguesClientImp;

public class InjectionConfigs extends AbstractModule {

    @Override
    protected void configure() {

        bind(LeaguesClient.class).to(LeaguesClientImp.class);
    }
}
