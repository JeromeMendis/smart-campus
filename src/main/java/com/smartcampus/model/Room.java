package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

/*
Represents a physical room on the Smart Campus.
Each room can contain multiple sensors.
*/
public class Room {

    // Unique identifier for the room (e.g. "LIB-301")
    private String id;

    // Human-readable name (e.g. "Library Quiet Study")
    private String name;

    // Maximum occupancy for safety regulations
    private int capacity;

    // List of sensor IDs deployed in this room
    // Acts as a foreign key reference to sensors
    private List<String> sensorIds = new ArrayList<>();

    // Default constructor required for JSON deserialization
    public Room() {}

    // Constructor for creating a room with basic details
    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<String> getSensorIds() { return sensorIds; }
    public void setSensorIds(List<String> sensorIds) { 
        this.sensorIds = sensorIds; 
    }
}