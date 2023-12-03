package com.epam.training.ticketservice.exceptions;

public class MovieAlreadyExistsException extends RuntimeException {
    public MovieAlreadyExistsException() {
        super("Movie already exists");
    }
}
