package com.codeflu.postgreson.db;

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
import java.sql.SQLException;

@RegisterRowMapper(ViewDao.ResultSetMapper.class)
public interface ViewDao {

    @SqlUpdate("INSERT INTO json_b (json) VALUES (:json)")
    @GetGeneratedKeys("id")
    String createSource(@Bind("json") @Json JsonNode jsonNode);

    @SqlUpdate("<view>")
    void createView(@Define("view") String view);

    @SqlQuery("<query>")
    PgResultSetMetaData  query(@Define("view") String view);

    @SqlQuery("SELECT * FROM public.<view> LIMIT 1")
    PgResultSetMetaData viewMeta(@Define("view") String view);

    class ResultSetMapper implements RowMapper<PgResultSetMetaData> {
        @Override
        public PgResultSetMetaData map(ResultSet rs, StatementContext ctx) throws SQLException {
            return ((PgResultSetMetaData)rs.getMetaData());
        }
    }

}
