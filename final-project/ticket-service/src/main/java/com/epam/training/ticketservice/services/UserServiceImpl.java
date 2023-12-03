package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.exceptions.UserAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.UserNotFoundException;
import com.epam.training.ticketservice.exceptions.UserNotSignedInException;
import com.epam.training.ticketservice.models.User;
import com.epam.training.ticketservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private UserDto currentUser;

    @Override
    public void signIn(String username, String password) throws UserNotFoundException, IllegalArgumentException {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Login failed due to incorrect credentials");
        }

        currentUser = new UserDto(
                user.get().getUsername(),
                user.get().getPassword(),
                user.get().getRole()
        );
    }

    @Override
    public void adminSignIn(String username, String password) throws IllegalArgumentException {
        Optional<User> adminUser = userRepository.findByUsernameAndPasswordAndRole(username, password, "admin");

        if (adminUser.isEmpty()) {
            throw new IllegalArgumentException("Login failed due to incorrect credentials");
        }

        currentUser = new UserDto(
                adminUser.get().getUsername(),
                adminUser.get().getPassword(),
                adminUser.get().getRole()
        );
    }

    @Override
    public void signOut() throws UserNotSignedInException {
        if (currentUser == null) {
            throw new UserNotSignedInException();
        }

        currentUser = null;
    }

    @Override
    public Optional<UserDto> describe() throws UserNotSignedInException {
        if (currentUser == null) {
            throw new UserNotSignedInException();
        }

        return Optional.of(currentUser);
    }

    @Override
    public void signUp(String username, String password, String role) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        Optional<User> user = Optional.of(new User(username, password, role));

        userRepository.save(user.get());
    }

    @Override
    public void deleteUser(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        userRepository.delete(user.get());
    }
}
