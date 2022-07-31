package com.codeflu.postgreson.api.resource;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.ResponseMetered;
import com.codahale.metrics.annotation.Timed;
import com.codeflu.postgreson.api.service.JsonBService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Slf4j
@Path("json")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class JsonBResource {

    private final JsonBService jsonbService;

    public JsonBResource(JsonBService jsonbService) {
        this.jsonbService = jsonbService;
    }

    @POST
    @Timed
    @ResponseMetered
    @ExceptionMetered
    public Response add(@Valid JsonNode request){
        log.info("Request : {}", request);
        return Response.ok(request).build();
    }

}
