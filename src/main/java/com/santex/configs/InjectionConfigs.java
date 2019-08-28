package com.santex.configs;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.santex.application.Application;
import com.santex.clients.FootballApiClient;
import com.santex.clients.imp.FootballApiClientImp;
import com.santex.daos.CompetitionsDao;
import com.santex.daos.TeamsDao;
import com.santex.daos.imp.CompetitionsDaoImp;
import com.santex.daos.imp.TeamsDaoImp;
import com.santex.services.CompetitionsService;
import com.santex.services.TeamsService;
import com.santex.services.imp.CompetitionsServiceImp;
import com.santex.services.imp.TeamsServiceImp;

import javax.inject.Named;

public class InjectionConfigs extends AbstractModule {

    @Override
    protected void configure() {
        //Services
        bind(CompetitionsService.class).to(CompetitionsServiceImp.class);
        bind(TeamsService.class).to(TeamsServiceImp.class);

        //Clients
        bind(FootballApiClient.class).to(FootballApiClientImp.class);

        //Daos
        bind(CompetitionsDao.class).to(CompetitionsDaoImp.class);
        bind(TeamsDao.class).to(TeamsDaoImp.class);
    }

    @Provides
    @Named(Constants.Headers.FOOTBAL_ORG_TOKEN)
    public String getApiKey() {
        return Application.getenv(Constants.Headers.FOOTBAL_ORG_TOKEN, "");
    }
}
