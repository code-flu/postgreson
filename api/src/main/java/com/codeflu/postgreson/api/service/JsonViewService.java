package com.codeflu.postgreson.api.service;

import com.codeflu.postgreson.core.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.jdbc.PgResultSetMetaData;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;

@Slf4j
public class JsonViewService {

    private final JsonView jsonView;

    public JsonViewService(DataSource dataSource) {
        this.jsonView = new JsonView(dataSource);
    }

    public ImmutableMap<String, PgResultSetMetaData> createView(JsonNode jsonNode){
        return jsonView.create(jsonNode);
    }

    public String query(String table, String query){

        return null;
    }

}
