package com.santex.configs;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.santex.clients.imp.FootballApiClientImp;
import kong.unirest.UnirestInstance;
import org.mockito.Mockito;

import javax.inject.Named;
import java.util.concurrent.ConcurrentHashMap;

public class RestClientTestConfigs extends AbstractModule {

    public static final ConcurrentHashMap<String, UnirestInstance> restClients = new ConcurrentHashMap<>();

    @Provides
    @Named(FootballApiClientImp.CLIENT_NAME)
    public UnirestInstance leaguesClient() {
        return buildDefaultRestClient(FootballApiClientImp.CLIENT_NAME);
    }

    private UnirestInstance buildDefaultRestClient(String clientName) {

        UnirestInstance restClient = restClients.get(clientName);
        if (restClient == null) {
            restClient = Mockito.mock(UnirestInstance.class);
            restClients.put(clientName, restClient);
        }

        return restClient;
    }

    /**
     * Shut down all registered clients
     */
    public static void shutDownClients() {
        restClients.forEach((k, v) -> v.shutDown());
    }
}
