package com.epam.training.ticketservice.exceptions;

public class ScreeningDoesNotExistException extends RuntimeException {
    public ScreeningDoesNotExistException() {
        super("There are no screenings");
    }
}
