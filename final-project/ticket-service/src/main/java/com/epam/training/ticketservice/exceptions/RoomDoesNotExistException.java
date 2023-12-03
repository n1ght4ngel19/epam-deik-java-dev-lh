package com.epam.training.ticketservice.exceptions;

public class RoomDoesNotExistException extends RuntimeException {
    public RoomDoesNotExistException() {
        super("Room does not exist");
    }
}
