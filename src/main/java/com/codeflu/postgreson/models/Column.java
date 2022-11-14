package com.codeflu.postgreson.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Column {

    private final String name;
    private final Type type;

    public static List<Column> of(Iterator<Map.Entry<String, JsonNode>> fields){
        return toColumns(fields);
    }

    public enum Type{
        TEXT,
        VARCHAR,
        ARRAY,
        INTEGER,
        BIGINT,
        BOOLEAN,
        DECIMAL,
        JSONB,
    }


    // map json fields into columns
    private static List<Column> toColumns(Iterator<Map.Entry<String, JsonNode>> fields) {
        List<Column> columns = new ArrayList<>();
        fields.forEachRemaining(field -> {
            columns.add(new Column(field.getKey(), type(field.getValue())));
        });
        return columns;
    }

    private static Column.Type type(JsonNode jsonNode) {
        if (jsonNode.isTextual()) {
            return Column.Type.TEXT;
        } else if (jsonNode.isNumber()) {
            return jsonNode.canConvertToInt() ? Column.Type.INTEGER : Column.Type.BIGINT;
        } else if (jsonNode.isLong()) {
            return Column.Type.BIGINT;
        } else if (jsonNode.isFloat()) {
            return Column.Type.DECIMAL;
        } else if (jsonNode.isBoolean()) {
            return Column.Type.BOOLEAN;
        } else {
            return Column.Type.JSONB;
        }
    }
}
