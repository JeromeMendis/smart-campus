package com.smartcampus.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.smartcampus.exception.SensorUnavailableException;

/*
Exception mapper for SensorUnavailableException.
Converts the exception into an HTTP 403 Forbidden response.
Triggered when a reading is posted to a MAINTENANCE sensor.
*/
@Provider
public class SensorUnavailableExceptionMapper
    implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException e) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", "Forbidden");
        error.put("message", "Sensor is in MAINTENANCE and cannot accept readings.");

        return Response.status(403)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}