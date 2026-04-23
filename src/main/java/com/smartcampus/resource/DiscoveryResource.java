package com.smartcampus.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
Discovery endpoint for the Smart Campus API.
 
Implements HATEOAS (Hypermedia as the Engine of Application State) by providing links to all available resources.
This allows clients to discover the API without static documentation.
*/
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    /**
     * GET /api/v1/
     * Returns API metadata including version, contact, and resource links.
     */
    @GET
    public Response discover() {
        // Build the response with API metadata
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("version", "1.0");
        response.put("name", "Smart Campus API");
        response.put("contact", "admin@smartcampus.ac.uk");

        // Add HATEOAS links to primary resource collections
        Map<String, String> links = new LinkedHashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        response.put("resources", links);

        return Response.ok(response).build();
    }
}