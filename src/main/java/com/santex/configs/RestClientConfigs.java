package com.santex.configs;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.netflix.config.ConfigurationManager;
import com.santex.clients.imp.FootballApiClientImp;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RestClientConfigs extends AbstractModule {

    private static final String REST_CLIENT_KEY = "restClient.";
    private static final String SOCKET_TIMEOUT = ".socketTimeout";
    private static final String CONNECTION_TIMEOUT = ".connectionTimeout";
    private static final String MAX_CONNECTIONS = ".maxConnections";
    private static final String MAX_CONNECTIONS_PER_ROUTE = ".maxConnectionsPerRoute";
    private static final String AUTOMATIC_RETRIES = ".automaticRetries";

    private static final ConcurrentHashMap<String, UnirestInstance> restClients = new ConcurrentHashMap<>();

    @Provides
    @Named(FootballApiClientImp.CLIENT_NAME)
    public UnirestInstance leaguesClient() {
        return buildDefaultRestClient(FootballApiClientImp.CLIENT_NAME);
    }

    private UnirestInstance buildDefaultRestClient(String clientName) {

        UnirestInstance restClient = Unirest.spawnInstance();
        restClient.config()
                .socketTimeout(
                        ConfigurationManager.getConfigInstance().getInt(REST_CLIENT_KEY + clientName + SOCKET_TIMEOUT,
                                ConfigurationManager.getConfigInstance().getInt(REST_CLIENT_KEY + "default" + SOCKET_TIMEOUT, 5000)
                        )
                )
                .connectTimeout(
                        ConfigurationManager.getConfigInstance().getInt(REST_CLIENT_KEY + clientName + CONNECTION_TIMEOUT,
                                ConfigurationManager.getConfigInstance().getInt(REST_CLIENT_KEY + "default" + CONNECTION_TIMEOUT, 1000)
                        )
                )
                .concurrency(
                        ConfigurationManager.getConfigInstance().getInt(REST_CLIENT_KEY + clientName + MAX_CONNECTIONS,
                                ConfigurationManager.getConfigInstance().getInt(REST_CLIENT_KEY + "default" + MAX_CONNECTIONS, 100)
                        ),
                        ConfigurationManager.getConfigInstance().getInt(REST_CLIENT_KEY + clientName + MAX_CONNECTIONS_PER_ROUTE,
                                ConfigurationManager.getConfigInstance().getInt(REST_CLIENT_KEY + "default" + MAX_CONNECTIONS_PER_ROUTE, 20)
                        )
                )
                .automaticRetries(
                        ConfigurationManager.getConfigInstance().getBoolean(REST_CLIENT_KEY + clientName + AUTOMATIC_RETRIES,
                                ConfigurationManager.getConfigInstance().getBoolean(REST_CLIENT_KEY + "default" + AUTOMATIC_RETRIES, true)
                        )
                );
//                .setObjectMapper(new JacksonObjectMapper());

        logger.debug(String.format("RestClient[name:%s, socketTimeout:%d, connectionTimeout:%d, maxConnections:%d, maxConnectionsPerRoute:%d, automaticRetries:%s]",
                clientName,
                restClient.config().getSocketTimeout(),
                restClient.config().getConnectionTimeout(),
                restClient.config().getMaxConnections(),
                restClient.config().getMaxPerRoutes(),
                restClient.config().isAutomaticRetries())
        );

        restClients.put(clientName, restClient);

        return restClient;
    }

    /**
     * Shut down all registered clients
     */
    public static void shutDownClients() {
        restClients.forEach((k, v) -> v.shutDown());
    }
}
