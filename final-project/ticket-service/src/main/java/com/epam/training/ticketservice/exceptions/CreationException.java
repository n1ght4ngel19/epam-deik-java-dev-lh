package com.epam.training.ticketservice.exceptions;

public class CreationException extends Exception {
    public CreationException(String message) {
        super("CreationException: " + message);
    }
}
