package com.santex.configs;

import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.santex.exceptions.ServerErrorException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class DataBaseTestConfigs extends AbstractModule {

    Logger logger = LoggerFactory.getLogger(DataBaseTestConfigs.class);

    @Provides
    @Singleton
    public SessionFactory provideH2DBConnection() {

        SessionFactory sessionFactory;

        try {

            String url = "jdbc:h2:mem:testDb";
            String user = "";
            String pass = "";
            String showSQL = "false";
            String hbm2ddl = "create";

            sessionFactory = createSessionFactory(url, user, pass, showSQL, hbm2ddl);

        } catch (Exception e) {
            logger.error("Error connecting to the DB", e);
            throw new ServerErrorException(e);
        }

        return sessionFactory;
    }

    private SessionFactory createSessionFactory(String url, String user, String pass, String showSQL, String hbm2ddl) {

        ServiceRegistry standarServiceRegistry = new StandardServiceRegistryBuilder()
                .applySetting(Environment.DRIVER, "org.h2.Driver")
                .applySetting(Environment.URL, url)
                .applySetting(Environment.USER, user)
                .applySetting(Environment.PASS, pass)
                .applySetting(Environment.DIALECT, "org.hibernate.dialect.H2Dialect")
                .applySetting(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
                .applySetting(Environment.SHOW_SQL, showSQL)
                .applySetting(Environment.HBM2DDL_AUTO, hbm2ddl)
                .build();

        logger.info(
                "Internal DB conection | user: {} | endpoint: {}",
                defaultString(user),
                defaultString(url)
        );


        MetadataSources metadataSources = new MetadataSources(standarServiceRegistry);
        //Register @Entity classes
        List<Class<?>> entityClasses = EntityScanner.scanPackages("com.santex.models.entities").result();
        for (Class<?> annotatedClass : entityClasses) {
            metadataSources.addAnnotatedClass(annotatedClass);
        }

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        return metadata.buildSessionFactory();
    }
}
