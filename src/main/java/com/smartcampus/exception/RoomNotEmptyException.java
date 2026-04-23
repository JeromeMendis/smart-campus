package com.smartcampus.exception;

/*
Exception thrown when attempting to delete a room that still has sensors assigned to it.
Mapped to HTTP 409 Conflict by RoomNotEmptyExceptionMapper.
*/
public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException() {
        super("Room still contains active sensors");
    }
}