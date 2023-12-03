package com.epam.training.ticketservice.exceptions;

public class UserNotSignedInException extends RuntimeException {
    public UserNotSignedInException() {
        super("You are not signed in");
    }
}
