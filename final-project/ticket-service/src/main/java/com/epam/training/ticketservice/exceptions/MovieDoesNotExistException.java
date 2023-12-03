package com.epam.training.ticketservice.exceptions;

public class MovieDoesNotExistException extends RuntimeException {
    public MovieDoesNotExistException() {
        super("There are no movies at the moment");
    }
}
