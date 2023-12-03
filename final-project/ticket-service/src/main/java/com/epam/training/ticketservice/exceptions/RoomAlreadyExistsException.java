package com.epam.training.ticketservice.exceptions;

public class RoomAlreadyExistsException extends RuntimeException {
    public RoomAlreadyExistsException() {
        super("Room already exists");
    }
}
