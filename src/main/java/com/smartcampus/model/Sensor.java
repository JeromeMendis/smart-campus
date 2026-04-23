package com.smartcampus.model;

/*
Represents a sensor deployed in a campus room.
Sensors can be of different types (Temperature, CO2, Occupancy etc.) and can have different statuses (ACTIVE, MAINTENANCE, OFFLINE).
*/
public class Sensor {

    // Unique identifier (e.g. "TEMP-001")
    private String id;

    // Category of sensor (e.g. "Temperature", "CO2", "Occupancy")
    private String type;

    // Current operational state: "ACTIVE", "MAINTENANCE", or "OFFLINE"
    private String status;

    // Most recent measurement recorded by this sensor
    // Updated every time a new reading is posted
    private double currentValue;

    // Foreign key linking to the Room where this sensor is located
    private String roomId;

    // Default constructor required for JSON deserialization
    public Sensor() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { 
        this.currentValue = currentValue; 
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}