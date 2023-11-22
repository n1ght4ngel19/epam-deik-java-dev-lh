package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.UserDto;

import java.util.Optional;

public interface UserService {
    void signIn(String username, String password);

    void adminSignIn(String username, String password);

    void signOut();

    Optional<UserDto> describe();

    Optional<UserDto> signUp(String username, String password, String role);

    void deleteUser(String username);
}
