package com.smartcampus.datastore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

/*
Central in-memory data store for the Smart Campus API.

Uses ConcurrentHashMap instead of regular HashMap because:
- JAX-RS creates a new resource instance per request
- Multiple threads can access these maps simultaneously
- ConcurrentHashMap provides thread-safe operations
- Prevents race conditions and data corruption
 
No database is used - all data is stored in memory.
Data is lost when the server stops.
*/
public class DataStore {

    // Stores all rooms, keyed by room ID (e.g. "LIB-301")
    public static Map<String, Room> rooms = new ConcurrentHashMap<>();

    // Stores all sensors, keyed by sensor ID (e.g. "TEMP-001")
    public static Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    // Stores sensor readings, keyed by sensor ID
    // Each sensor has a list of historical readings
    public static Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
}