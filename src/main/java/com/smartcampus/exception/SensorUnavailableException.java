package com.smartcampus.exception;

/*
Exception thrown when a reading is posted to a sensor that is currently in MAINTENANCE status.
Mapped to HTTP 403 Forbidden by SensorUnavailableExceptionMapper.
*/
public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException() {
        super("Sensor is under maintenance and cannot accept readings");
    }
}