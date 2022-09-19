package com.codeflu.postgreson.api.resource.dto;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.Getter;

@Getter
public class Column {
    String name;
    String type;

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
