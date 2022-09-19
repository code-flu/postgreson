package com.codeflu.postgreson.api.resource.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.postgresql.jdbc.PgResultSetMetaData;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Getter
public class Table {
    String table;
    List<Column> columns;

    public Table(ImmutableMap<String, PgResultSetMetaData> resultSetMetaData) throws SQLException {
        this.table = resultSetMetaData.keySet().iterator().next();
        this.columns = toColumns(resultSetMetaData.get(table));
    }

    // map json fields into columns
    private List<Column> toColumns(PgResultSetMetaData fields) throws SQLException {
        List<Column> columns = new ArrayList<>();
        for(int i=1; i<=fields.getColumnCount(); i++){
            columns.add(new Column( fields.getColumnName(i), fields.getColumnTypeName(i)));
        }
        return columns;
    }
}
