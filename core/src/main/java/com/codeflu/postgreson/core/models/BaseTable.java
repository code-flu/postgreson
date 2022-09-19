package com.codeflu.postgreson.core.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.*;

@Getter
public class BaseTable {
    String table;
    List<Column> columns;

    public BaseTable(String table, Iterator<Map.Entry<String, JsonNode>> fields) {
        this.table = table;
        this.columns = toColumns(fields);
    }

    // map json fields into columns
    private List<Column> toColumns(Iterator<Map.Entry<String, JsonNode>> fields){
        List<Column> columns = new ArrayList<>();
        fields.forEachRemaining(field -> {

            columns.add(new Column(field.getKey(), type(field.getValue())));
        });
        return columns;
    }

    private Column.Type type(JsonNode jsonNode){
            if(jsonNode.isTextual()){
                return Column.Type.TEXT;
            }else if(jsonNode.isNumber()){
                return jsonNode.canConvertToInt()? Column.Type.INTEGER: Column.Type.BIGINT;
            }else if (jsonNode.isLong()){
                return Column.Type.BIGINT;
            }else if(jsonNode.isFloat()){
                return Column.Type.DECIMAL;
            }else if(jsonNode.isBoolean()){
                return Column.Type.BOOLEAN;
            }else {
                return Column.Type.JSONB;
            }
    }
}
