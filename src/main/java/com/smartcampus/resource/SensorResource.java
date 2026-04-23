package com.smartcampus.resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.datastore.DataStore;
import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Sensor;

/*
Resource class for managing sensors.
Handles all operations for the /api/v1/sensors endpoint.
*/
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> list = new ArrayList<>(DataStore.sensors.values());
        if (type != null && !type.isEmpty()) {
            list = list.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        }
        return Response.ok(list).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        // Validate that the referenced room exists
        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException();
        }

        // Check for duplicate sensor ID
        if (DataStore.sensors.containsKey(sensor.getId())) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("error", "Sensor already exists");
            return Response.status(409)
                .entity(error)
                .build();
        }

        // Save sensor to data store
        DataStore.sensors.put(sensor.getId(), sensor);

        // Initialize empty readings list for this sensor
        DataStore.readings.put(sensor.getId(), new ArrayList<>());

        // Link sensor ID to the room it belongs to
        DataStore.rooms.get(sensor.getRoomId())
            .getSensorIds().add(sensor.getId());

        return Response.status(201).entity(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(
            @PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}