package com.codeflu.postgreson.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

public class AppConfig extends Configuration {

    @Getter
    @JsonProperty("db")
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

}
