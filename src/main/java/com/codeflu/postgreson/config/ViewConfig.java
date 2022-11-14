package com.codeflu.postgreson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;

@Slf4j
public class ViewConfig {
    private final Jdbi jdbi;
    private final ObjectMapper mapper;

    public ViewConfig(@NonNull DataSource dataSource) {
        this.jdbi = initDb(dataSource);
        this.mapper = new ObjectMapper();
        createTable();
    }

    // init db
    private Jdbi initDb(DataSource dataSource) {
        log.info("Initializing jdbi");
        return Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin())
                .installPlugin(new PostgresPlugin())
                .installPlugin(new Jackson2Plugin())
                .registerArrayType(String.class, "text");
    }

    // create default table 'json_b'
    private void createTable() {
        this.jdbi.withHandle((handle) -> handle.execute("CREATE TABLE IF NOT EXISTS json_b (id VARCHAR (10) DEFAULT concat(substr(md5(random()::text), 24, 32), '_') NOT NULL, json JSONB NOT NULL, PRIMARY KEY (id))"));
    }

    protected Jdbi getJdbi() {
        return jdbi;
    }


    public ObjectMapper getMapper() {
        return this.mapper;
    }
}
