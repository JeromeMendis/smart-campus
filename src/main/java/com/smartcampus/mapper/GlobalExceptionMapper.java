package com.smartcampus.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/*
Global catch-all exception mapper.
Intercepts ANY unexpected runtime error and returns HTTP 500.
 
This ensures the API never leaks raw Java stack traces to clients.
Exposing stack traces is a security risk because they reveal:
- Internal class names and package structure
- Third-party library versions (attackers can find known vulnerabilities)
- Server file paths and directory structure
- Logic errors that attackers can exploit
*/
@Provider
public class GlobalExceptionMapper
    implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {
        // Print real error to server console for debugging
        e.printStackTrace();

        // Return generic error to client (no internal details exposed)
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred.");

        return Response.status(500)
            .entity(error)
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}