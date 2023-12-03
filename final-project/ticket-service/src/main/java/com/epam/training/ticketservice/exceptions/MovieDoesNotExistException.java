package com.epam.training.ticketservice.exceptions;

public class MovieDoesNotExistException extends RuntimeException {
    public MovieDoesNotExistException() {
        super("Movie does not exist");
    }
}
