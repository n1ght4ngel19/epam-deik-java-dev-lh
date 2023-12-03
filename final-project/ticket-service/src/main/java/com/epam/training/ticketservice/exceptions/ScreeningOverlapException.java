package com.epam.training.ticketservice.exceptions;

public class ScreeningOverlapException extends RuntimeException {
    public ScreeningOverlapException(String message) {
        super(message);
    }
}
