package com.codeflu.postgreson.api.dto;

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
