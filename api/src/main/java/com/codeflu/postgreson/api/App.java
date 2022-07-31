package com.codeflu.postgreson.api;

import com.codeflu.postgreson.api.config.AppConfig;
import com.codeflu.postgreson.api.config.FlywayFactory;
import com.codeflu.postgreson.api.dao.JsonBDao;
import com.codeflu.postgreson.api.resource.JsonBResource;
import com.codeflu.postgreson.api.service.JsonBService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;


@Slf4j
public class App extends Application<AppConfig> {

    @Override
    public void initialize(@NonNull Bootstrap<AppConfig> bootstrap) {
        super.initialize(bootstrap);
        // Enable variable substitution with environment variables.
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
    }

    @Override
    public void run(AppConfig config, Environment env) {

        log.info("Initializing data source factory");
        final DataSourceFactory sourceFactory = config.getDataSourceFactory();
        log.info("Initializing data source");
        final ManagedDataSource source = sourceFactory.build(env.metrics(), "");

        log.info("Applying migrations");
        if (config.isMigrateOnStartup()) {
            migrateDbOrError(config, sourceFactory);
        }

        log.info("Initializing jdbi");
        final JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(env, config.getDataSourceFactory(), source, "")
                .installPlugin(new SqlObjectPlugin())
                .installPlugin(new PostgresPlugin())
                .installPlugin(new Jackson2Plugin());
        jdbi.registerArrayType(String.class, "text");
        log.info("Jdbi successfully initialized");

        log.info("Initializing resources");
        registerResources(config, env, jdbi);
        log.info("Initialized resources");
    }

    // register dao, service and resource
    private void registerResources(AppConfig config, Environment env, Jdbi jdbi) {
        log.info("Initializing JsonBDao");
        JsonBDao jsonbDao = jdbi.onDemand(JsonBDao.class);

        log.info("Initializing JsonBService");
        JsonBService jsonBService = new JsonBService(jsonbDao);

        log.info("Initializing JsonBResource");
        JsonBResource jsonBResource = new JsonBResource(jsonBService);

        log.info("Registering jsonBResource as an api to Jersey");
        env.jersey().register(jsonBResource);
    }

    // migrating sql schema to the database
    private void migrateDbOrError(@NonNull AppConfig config, @NonNull DataSourceFactory source) {

        final FlywayFactory flywayFactory = config.getFlywayFactory();
        final Flyway flyway = flywayFactory.build(source);

        try {
            log.info("Migrating schemas...");
            final int migrations = flyway.migrate();
            log.info("Successfully applied '{}' migrations to database.", migrations);
        } catch (FlywayException errorOnDbMigrate) {
            log.error("Failed to apply migration to database.", errorOnDbMigrate);
            try {
                log.info("Repairing failed database migration...");
                flyway.repair();
                log.info("Successfully repaired database.");
            } catch (FlywayException errorOnDbRepair) {
                log.error("Failed to apply repair to database.", errorOnDbRepair);
            }

            log.info("Stopping app...");
            // Propagate throwable up the stack.
            onFatalError(errorOnDbMigrate); // Signal app termination.
        }
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
