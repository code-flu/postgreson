package com.codeflu.postgreson.core.models;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.Getter;

@Getter
public class Column {
    String name;
    Type type;

    public Column(String name, Type type) {
        this.name = name;
        this.type = type;
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
}
