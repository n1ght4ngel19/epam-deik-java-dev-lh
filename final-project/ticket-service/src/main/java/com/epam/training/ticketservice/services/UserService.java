package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.exceptions.UserAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.UserNotFoundException;
import com.epam.training.ticketservice.exceptions.UserNotSignedInException;

import java.util.Optional;

public interface UserService {
    void signIn(String username, String password) throws UserNotFoundException;

    void adminSignIn(String username, String password);

    void signOut() throws UserNotFoundException;

    Optional<UserDto> describe() throws UserNotSignedInException;

    void signUp(String username, String password, String role) throws UserAlreadyExistsException;

    void deleteUser(String username) throws UserNotFoundException;
}
