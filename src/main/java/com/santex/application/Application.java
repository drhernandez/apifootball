package com.santex.application;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.santex.configs.Injectors;
import com.santex.routers.ErrorHandlerRouter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import spark.RouteGroup;
import spark.servlet.SparkApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import static com.santex.configs.Injectors.APP;
import static spark.Spark.*;

@Slf4j
public abstract class Application implements SparkApplication, RouteGroup {

    /**
     * Overloadable Configurations
     */
    public static final String ENVIRONMENT = "env";
    public static final String DEVELOP = "dev";
    public static final String PRODUCTION = "prod";

    public static final String SERVER_PORT = "server.port";
    public static final String SERVER_ADDRESS = "server.address";
    public static final String SERVER_BASE_THREADS = "server.base.threads";
    public static final String SERVER_MAX_CORE_MULTIPLIER = "server.max.core.multiplier";
    public static final String SERVER_MIN_CORE_MULTIPLIER = "server.min.core.multiplier";
    public static final String SERVER_REQUEST_TIMEOUT = "server.request.timeout";
    public static final String APPLICATION_PATH = "application.path";

    /**
     * MDC key to identify a request
     */
    public static final String REQUEST_ID = "request_id";

    /**
     * Server configuration variables
     */
    private int port;
    private String address;
    private int baseThreads;
    private int maxMultiplier;
    private int minMultiplier;
    private int timeout;
    private String basePath;

    /**
     * Environment variable
     */
    @Getter
    private static String environment = getenv(ENVIRONMENT, DEVELOP);

    /**
     * Create an application with default configuration.
     */
    public Application() {
        try {
            setUpConfigurationManagement();
            initializeWithDefaults();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Initiate the application with the configuration and dependency injection module loaded.
     */
    @Override
    public void init() {
        configureServer();
        configureMiddlewares();
        awaitInitialization();
    }

    /**
     * Destroy the application
     */
    @Override
    public void destroy() {
        stop();
        Injectors.clearInjectors();
    }

    /**
     * Configure the embedded jetty server to start.
     */
    private void configureServer() {
        final int processors = Runtime.getRuntime().availableProcessors();
        final int maxThreads = processors * maxMultiplier + baseThreads;
        final int minThreads = processors * minMultiplier + baseThreads;

        port(port);
        ipAddress(address);
        threadPool(maxThreads, minThreads, timeout);

        logger.info("Listening in {}:{} using thread pool: [min:{} | max:{} | timeout:{}]", address, port, minThreads, maxThreads, timeout);
    }

    /**
     * Configure default spark middlewares
     */
    private void configureMiddlewares() {

        before((request, response) -> MDC.put(REQUEST_ID, UUID.randomUUID().toString()));
        after((request, response) -> MDC.clear());

        path(basePath, this);
        Injectors.getInjector(APP).getInstance(ErrorHandlerRouter.class).register();
    }

    /**
     * Configure the context variables of the cascaded properties.
     *
     * @environment="dev" or "prod"
     * @stack="scope"
     */
    private void setUpConfigurationManagement() {
        try {
            DeploymentContext context = ConfigurationManager.getDeploymentContext();
            context.setDeploymentEnvironment(isProduction() ? PRODUCTION : DEVELOP);
            ConfigurationManager.loadCascadedPropertiesFromResources("config");

        } catch (IOException e) {
            logger.error("Error setting up configuration management", e);
        }
    }

    /**
     * Initialize application configuration fields with defaults.
     *
     * @throws UnknownHostException
     */
    private void initializeWithDefaults() throws UnknownHostException {

        port = ConfigurationManager.getConfigInstance().getInt(SERVER_PORT, 8080);
        final String ipAddress = InetAddress.getLocalHost().getHostAddress();
        address = ConfigurationManager.getConfigInstance().getString(SERVER_ADDRESS, ipAddress);
        baseThreads = ConfigurationManager.getConfigInstance().getInt(SERVER_BASE_THREADS, 3);
        maxMultiplier = ConfigurationManager.getConfigInstance().getInt(SERVER_MAX_CORE_MULTIPLIER, 2);
        minMultiplier = ConfigurationManager.getConfigInstance().getInt(SERVER_MIN_CORE_MULTIPLIER, 2);
        timeout = ConfigurationManager.getConfigInstance().getInt(SERVER_REQUEST_TIMEOUT, 5000);
        basePath = ConfigurationManager.getConfigInstance().getString(APPLICATION_PATH, "/");
    }

    /**
     * Return the value of the specified environment variable. An
     * environment variable is a system-dependent external named
     * value.
     * <p>
     * If not present return the default value
     *
     * @param var
     * @param defaultValue
     * @return
     */
    public static String getenv(String var, String defaultValue) {
        final String env = System.getenv(var);
        if (env == null)
            return defaultValue;
        return env;
    }

    /**
     * Test if it is a productive environment
     */
    public static boolean isProduction() {
        return environment.equalsIgnoreCase(PRODUCTION);
    }
}
