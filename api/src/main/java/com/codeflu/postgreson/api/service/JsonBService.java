package com.codeflu.postgreson.api.service;

import com.codeflu.postgreson.api.dao.JsonBDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonBService {

    private final JsonBDao jsonbDao;

    public JsonBService(JsonBDao jsonbDao) {
        this.jsonbDao = jsonbDao;
    }

}
