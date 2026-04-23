package com.smartcampus.model;

/*
Represents a single historical reading from a sensor.
Each reading captures a value at a specific point in time.
*/
public class SensorReading {

    // Unique reading event ID (UUID recommended)
    private String id;

    // Epoch time in milliseconds when the reading was captured
    private long timestamp;

    // The actual metric value recorded by the sensor hardware
    private double value;

    // Default constructor required for JSON deserialization
    public SensorReading() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}