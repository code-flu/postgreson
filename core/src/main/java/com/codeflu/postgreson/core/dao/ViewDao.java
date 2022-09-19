package com.codeflu.postgreson.core.dao;

import com.fasterxml.jackson.databind.JsonNode;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.json.Json;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.postgresql.jdbc.PgResultSetMetaData;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface ViewDao {

    @SqlUpdate("INSERT INTO json_b (json) VALUES (:json)")
    @GetGeneratedKeys("id")
    String createSource(@Bind("json") @Json JsonNode jsonNode);

    @SqlUpdate("<view>")
    void createView(@Define("view") String view);

    @SqlQuery("SELECT * FROM <view> LIMIT 1")
    @RegisterRowMapper(ResultSetMapper.class)
    PgResultSetMetaData viewMeta(@Define("view") String view);

    class ResultSetMapper implements RowMapper<PgResultSetMetaData> {

        @Override
        public PgResultSetMetaData map(ResultSet rs, StatementContext ctx) throws SQLException {
            return ((PgResultSetMetaData)rs.getMetaData());
        }
    }

}
