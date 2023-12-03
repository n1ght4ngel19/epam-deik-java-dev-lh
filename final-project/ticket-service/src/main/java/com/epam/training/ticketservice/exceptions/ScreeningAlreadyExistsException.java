package com.epam.training.ticketservice.exceptions;

public class ScreeningAlreadyExistsException extends RuntimeException {
    public ScreeningAlreadyExistsException() {
        super("Screening already exists");
    }
}
