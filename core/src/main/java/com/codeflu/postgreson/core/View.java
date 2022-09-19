package com.codeflu.postgreson.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import org.postgresql.jdbc.PgResultSetMetaData;

import java.sql.ResultSetMetaData;

public interface View {
    /**
     * Takes json string as parameter and parse into list of columns. Basically, these columns are json fields.
     *
     * @param json string
     * @return returns table with its column name and type
     */
    ImmutableMap<String, PgResultSetMetaData> create(String json) throws JsonProcessingException;

    /**
     * Takes JsonNode object as parameter and parse into list of columns. Basically, these columns are json fields.
     *
     * @param jsonNode object
     * @return returns table with its column name and type
     */
    ImmutableMap<String, PgResultSetMetaData> create(JsonNode jsonNode);
}
