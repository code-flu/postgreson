package com.codeflu.postgreson.api.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

public class AppConfig extends Configuration {

    private final boolean DEFAULT_MIGRATE_ON_STARTUP = true;

    public boolean isMigrateOnStartup() {
        return DEFAULT_MIGRATE_ON_STARTUP;
    }

    @Getter
    @JsonProperty("db")
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @Getter
    @JsonProperty("flyway")
    private final FlywayFactory flywayFactory = new FlywayFactory();

}
