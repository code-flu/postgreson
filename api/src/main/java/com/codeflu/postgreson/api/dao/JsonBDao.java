package com.codeflu.postgreson.api.dao;

import com.fasterxml.jackson.databind.JsonNode;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface JsonBDao {

    @SqlUpdate("INSERT INTO json_b (json) VALUES (:json)")
    @GetGeneratedKeys("id")
    UUID insert(@BindBean Row row);

    class Row {
        @NotNull UUID id;
        @NotNull JsonNode json;

        @Json
        public JsonNode getJson() {
            return json;
        }
    }
}
