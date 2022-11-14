package com.codeflu.postgreson;

import com.codeflu.postgreson.api.JsonViewResource;
import com.codeflu.postgreson.config.AppConfig;
import com.codeflu.postgreson.service.JsonViewService;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;

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

        log.info("Initializing jdbi instance");
        Jdbi jdbi = initDb(source);

        registerResources(env, jdbi);

        log.info("Creating table 'json_b' if not exists");
        createTable(jdbi);
    }

    // register dao, service and resource
    private void registerResources(Environment env, Jdbi jdbi) {

        log.info("Initializing Services");
        JsonViewService jsonViewService = new JsonViewService(jdbi);

        log.info("Initializing Resource");
        JsonViewResource jsonViewResource = new JsonViewResource(jsonViewService);

        log.info("Registering jsonBResource as an api to Jersey");
        env.jersey().register(jsonViewResource);
    }

    // init db
    private Jdbi initDb(DataSource dataSource) {
        return Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin())
                .installPlugin(new PostgresPlugin())
                .installPlugin(new Jackson2Plugin())
                .registerArrayType(String.class, "text");
    }

    // create default table 'json_b'
    private void createTable(Jdbi jdbi) {
        jdbi.withHandle((handle) -> handle.execute("CREATE TABLE IF NOT EXISTS json_b (id VARCHAR (10) DEFAULT concat('t', substr(md5(random()::text), 25, 32), '_') NOT NULL, json JSONB NOT NULL, PRIMARY KEY (id))"));
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
