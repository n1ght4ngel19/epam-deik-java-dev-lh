package com.epam.training.ticketservice.exceptions;

public class ScreeningCannotBeCreatedException extends RuntimeException {
    public ScreeningCannotBeCreatedException() {
        super("Screening cannot be created");
    }
}
