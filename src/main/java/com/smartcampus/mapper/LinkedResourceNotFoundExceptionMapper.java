package com.smartcampus.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.smartcampus.exception.LinkedResourceNotFoundException;

/*
Exception mapper for LinkedResourceNotFoundException.
Converts the exception into an HTTP 422 Unprocessable Entity response.
Triggered when a sensor references a roomId that doesn't exist.
 
422 is used instead of 404 because:
- The URL /api/v1/sensors is valid (not missing)
- The JSON payload is well-formed (not malformed)
- The issue is a semantic validation failure inside the payload
*/
@Provider
public class LinkedResourceNotFoundExceptionMapper
    implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException e) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", "Unprocessable Entity");
        error.put("message", "The specified roomId does not exist.");

        return Response.status(422)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}