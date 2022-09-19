package com.codeflu.postgreson.api.resource;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.ResponseMetered;
import com.codahale.metrics.annotation.Timed;
import com.codeflu.postgreson.api.resource.dto.Table;
import com.codeflu.postgreson.api.service.JsonViewService;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.jdbc.PgResultSetMetaData;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Slf4j
@Path("json")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class JsonViewResource {

    private final JsonViewService jsonbService;

    public JsonViewResource(JsonViewService jsonbService) {
        this.jsonbService = jsonbService;
    }

    @POST
    @Timed
    @ResponseMetered
    @ExceptionMetered
    public Response add(@Valid JsonNode request) throws SQLException {
        log.debug("Request : {}", request);
        ImmutableMap<String, PgResultSetMetaData> resultSetMetaData = jsonbService.createView(request);
        return Response.ok(new Table(resultSetMetaData)).build();
    }

    @POST
    @Timed
    @ResponseMetered
    @ExceptionMetered
    @Path("{table}/query")
    public Response add(@PathParam("table") String table, @Valid String query) {
        log.debug("query : {}", query);
        return Response.ok(jsonbService.query(table, query)).build();
    }

}
