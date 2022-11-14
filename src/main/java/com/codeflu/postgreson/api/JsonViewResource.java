package com.codeflu.postgreson.api;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.ResponseMetered;
import com.codahale.metrics.annotation.Timed;
import com.codeflu.postgreson.api.dto.Table;
import com.codeflu.postgreson.service.JsonViewService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.jdbc.PgResultSetMetaData;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Slf4j
@Path("json")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class JsonViewResource {

    private final JsonViewService jsonViewService;

    public JsonViewResource(JsonViewService jsonViewService) {
        this.jsonViewService = jsonViewService;
    }

    @POST
    @Timed
    @ResponseMetered
    @ExceptionMetered
    public Response add(@Valid JsonNode request) throws SQLException {
        log.debug("Request : {}", request);
        Map<String, PgResultSetMetaData> resultSetMetaData = jsonViewService.create(request);
        return Response.ok(new Table(resultSetMetaData)).build();
    }

    @POST
    @Timed
    @ResponseMetered
    @ExceptionMetered
    @Path("{table}/query")
    public Response add(@PathParam("table") String table, @Valid String query) {
        log.debug("query : {}", query);
        return Response.ok().build();
    }

}
