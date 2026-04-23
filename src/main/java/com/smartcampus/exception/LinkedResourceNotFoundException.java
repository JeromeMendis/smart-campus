package com.smartcampus.exception;

/*
Exception thrown when a sensor is created with a roomId that does not exist in the system.
Mapped to HTTP 422 Unprocessable Entity by LinkedResourceNotFoundExceptionMapper.
*/
public class LinkedResourceNotFoundException extends RuntimeException {
    public LinkedResourceNotFoundException() {
        super("Referenced room does not exist");
    }
}