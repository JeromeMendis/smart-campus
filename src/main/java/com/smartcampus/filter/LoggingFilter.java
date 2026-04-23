package com.smartcampus.filter;

import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/*
JAX-RS filter for logging all API requests and responses.
 
Implements both ContainerRequestFilter and ContainerResponseFilter to intercept every incoming request and outgoing response.
 
Using a filter for logging is better than manual Logger.info() calls because:
- DRY principle: logging logic is defined once, applied everywhere
- No risk of forgetting to log a new endpoint
- Separates cross-cutting concerns from business logic
- Easy to update logging format in one place
 
The @Provider annotation registers this filter automatically with Jersey.
*/
@Provider
public class LoggingFilter
    implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER =
        Logger.getLogger(LoggingFilter.class.getName());

    /*
    Logs incoming HTTP requests.
    Called before the request reaches the resource method.
    */
    @Override
    public void filter(ContainerRequestContext req) {
        LOGGER.info("REQUEST: "
            + req.getMethod() + " "
            + req.getUriInfo().getRequestUri());
    }

    /*
    Logs outgoing HTTP responses.
    Called after the resource method returns a response.
    */
    @Override
    public void filter(ContainerRequestContext req,
                       ContainerResponseContext res) {
        LOGGER.info("RESPONSE STATUS: " + res.getStatus());
    }
}