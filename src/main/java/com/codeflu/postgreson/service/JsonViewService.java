package com.codeflu.postgreson.service;

import com.codeflu.postgreson.db.ViewDao;
import com.codeflu.postgreson.models.Column;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.postgresql.jdbc.PgResultSetMetaData;

import java.util.List;
import java.util.Map;

@Slf4j
public class JsonViewService {

    private final Jdbi jdbi;
    private final ObjectMapper mapper;

    public JsonViewService(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.mapper = new ObjectMapper();
    }

    public Map<String, PgResultSetMetaData> create(JsonNode jsonNode) throws UnableToExecuteStatementException {

        if(jsonNode.getNodeType().equals(JsonNodeType.ARRAY)) throw new IllegalArgumentException("Accepts only the json object.");

        return jdbi.inTransaction(handle -> {

            ViewDao dao = handle.attach(ViewDao.class);

            // insert requested json into `json_b` table
            String viewName = dao.createSource(jsonNode);
            log.info("View {}", viewName);

            // parse json fields into List<Column>
            List<Column> columns = Column.of(jsonNode.fields());

            // Build view statement from columns
            String viewStatement = ViewBuilder.build(viewName, columns);
            log.info("\n{}",viewStatement);

            // create view
            dao.createView(viewStatement);

            return Map.of(viewName, dao.viewMeta(viewName));
        });

    }

    public void query(String query) {
        jdbi.inTransaction(handle -> {

            ViewDao dao = handle.attach(ViewDao.class);

            // insert source json into `json_b` table
            PgResultSetMetaData  resultSetMetaData = dao.query(query);

            return null;
        });
    }
}
