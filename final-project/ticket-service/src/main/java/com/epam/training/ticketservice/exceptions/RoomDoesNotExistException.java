package com.epam.training.ticketservice.exceptions;

public class RoomDoesNotExistException extends RuntimeException {
    public RoomDoesNotExistException() {
        super("There are no rooms at the moment");
    }
}
