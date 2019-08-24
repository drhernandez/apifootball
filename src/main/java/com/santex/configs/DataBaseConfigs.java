package com.santex.configs;

import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.netflix.config.ConfigurationManager;
import com.santex.application.Application;
import com.santex.exceptions.DataBaseException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultString;

@Slf4j
public class DataBaseConfigs extends AbstractModule {

    @Provides @Singleton
    public SessionFactory provideDBConnection() {

        SessionFactory sessionFactory;

        try {

            String endpoint = Application.getenv(ConfigurationManager.getConfigInstance().getString("database.endpoint"), "");
            String pass = Application.getenv(ConfigurationManager.getConfigInstance().getString("database.pass"), "");
            String dbName = ConfigurationManager.getConfigInstance().getString("database.name");
            String parameters = ConfigurationManager.getConfigInstance().getString("database.parameters");
            String url = String.format("jdbc:mysql://%s/%s?%s", endpoint, dbName, parameters);
            String user = ConfigurationManager.getConfigInstance().getString("database.user");
            String showSQL = ConfigurationManager.getConfigInstance().getString("database.showSqlFlag", "false");
            String hbm2ddl = ConfigurationManager.getConfigInstance().getString("database.hbm2ddl", "none");

            sessionFactory = createSessionFactory(url, user, pass, showSQL, hbm2ddl);

        } catch (Exception e) {
            logger.error("Error trying to create SessionFactory", e);
            throw new DataBaseException("SessionFactory creation exception", e);
        }

        return sessionFactory;
    }

    private SessionFactory createSessionFactory(String url, String user, String pass, String showSQL, String hbm2ddl) {

        ServiceRegistry standarServiceRegistry = new StandardServiceRegistryBuilder()
                .applySetting(Environment.DRIVER, "com.mysql.jdbc.Driver")
                .applySetting(Environment.URL, url)
                .applySetting(Environment.USER, user)
                .applySetting(Environment.PASS, pass)
                .applySetting(Environment.DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect")
                .applySetting(Environment.SHOW_SQL, showSQL)
                .applySetting(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
                .applySetting(Environment.HBM2DDL_AUTO, hbm2ddl)
                .applySetting(Environment.AUTOCOMMIT, false)
                // CONNECTION POOL CONFIG
                .applySetting(Environment.C3P0_MIN_SIZE, 5)
                .applySetting(Environment.C3P0_MAX_SIZE, 20)
                .applySetting(Environment.C3P0_MAX_STATEMENTS, 50)
                .applySetting(Environment.C3P0_ACQUIRE_INCREMENT, 5)
                .applySetting(Environment.C3P0_IDLE_TEST_PERIOD, 300) // 5 min
                .applySetting(Environment.C3P0_TIMEOUT, 10800) // 3hs
                .build();

        logger.info(
                "Internal DB conection | user: {} | endpoint: {}",
                defaultString(user),
                defaultString(url)
        );


        MetadataSources metadataSources = new MetadataSources(standarServiceRegistry);
        //Register @Entity classes
        List<Class<?>> entityClasses = EntityScanner.scanPackages("com.santex.models").result();
        for (Class<?> annotatedClass : entityClasses) {
            metadataSources.addAnnotatedClass(annotatedClass);
        }

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        return metadata.buildSessionFactory();
    }
}
