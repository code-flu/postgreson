package com.codeflu.postgreson.api;

import com.codeflu.postgreson.api.config.AppConfig;
import com.codeflu.postgreson.api.resource.JsonViewResource;
import com.codeflu.postgreson.api.service.JsonViewService;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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

        log.info("Initializing resources");
        registerResources(config, env, source);
        log.info("Initialized resources");
    }

    // register dao, service and resource
    private void registerResources(AppConfig config, Environment env, ManagedDataSource dataSource) {

        log.info("Initializing JsonBService");
        JsonViewService jsonViewService = new JsonViewService(dataSource);

        log.info("Initializing JsonBResource");
        JsonViewResource jsonViewResource = new JsonViewResource(jsonViewService);

        log.info("Registering jsonBResource as an api to Jersey");
        env.jersey().register(jsonViewResource);
    }

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }
}
