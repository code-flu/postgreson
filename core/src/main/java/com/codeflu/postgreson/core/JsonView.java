package com.codeflu.postgreson.core;

import com.codeflu.postgreson.core.config.ViewConfig;
import com.codeflu.postgreson.core.dao.ViewDao;
import com.codeflu.postgreson.core.models.BaseTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.jdbc.PgResultSetMetaData;

import javax.sql.DataSource;

@Slf4j
public class JsonView extends ViewConfig implements View {

    public JsonView(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ImmutableMap<String, PgResultSetMetaData> create(String json) throws JsonProcessingException {
        JsonNode jsonNode = getMapper().readTree(json);
        return create(jsonNode);
    }

    @Override
    public ImmutableMap<String, PgResultSetMetaData> create(JsonNode jsonNode) {

        if(jsonNode.getNodeType().equals(JsonNodeType.ARRAY)) throw new IllegalArgumentException("Only supports json object");

        return getJdbi().inTransaction(handle -> {

            ViewDao dao = handle.attach(ViewDao.class);

            // insert source json into `json_b` table
            String viewName = dao.createSource(jsonNode);

            // map json field into BaseTable obj
            BaseTable baseTable = new BaseTable(viewName, jsonNode.fields());

            // Build sql statement for base view
            String baseView = QueryBuilder.baseView(baseTable.getTable(), baseTable.getColumns());
            log.info("\n{}",baseView);

            // create base view
            dao.createView(baseView);
            return ImmutableMap.of(viewName, dao.viewMeta(viewName));
        });

    }

    public void query(String query) {

    }

}
