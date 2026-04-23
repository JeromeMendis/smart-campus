package com.smartcampus.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.smartcampus.exception.RoomNotEmptyException;

/*
Exception mapper for RoomNotEmptyException.
Converts the exception into an HTTP 409 Conflict response.
Triggered when a room with active sensors is deleted.
*/
@Provider
public class RoomNotEmptyExceptionMapper
    implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException e) {
        // Build structured JSON error response
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", "Conflict");
        error.put("message", "Cannot delete room. It still has sensors assigned.");

        return Response.status(409)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}