package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.UserDto;
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
    public void signIn(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        user.ifPresentOrElse(
                (value) -> {
                    currentUser = new UserDto(value.getUsername(), value.getPassword(), value.getRole());
                },
                () -> {
                    throw new IllegalArgumentException("Login failed due to incorrect credentials");
                }
        );
    }

    @Override
    public void adminSignIn(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPasswordAndRole(username, password, "admin");

        user.ifPresentOrElse(
                (value) -> {
                    currentUser = new UserDto(value.getUsername(), value.getPassword(), value.getRole());
                },
                () -> {
                    throw new IllegalArgumentException("Login failed due to incorrect credentials");
                }
        );
    }

    @Override
    public void signOut() {
        currentUser = null;
    }

    @Override
    public Optional<UserDto> describe() {
        return Optional.ofNullable(currentUser);
    }

    @Override
    public Optional<UserDto> signUp(String username, String password, String role) {
        Optional<User> user = Optional.of(new User(username, password, role));

        userRepository.save(user.get());

        return Optional.of(new UserDto(username, password, role));
    }

    @Override
    public void deleteUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        user.ifPresent(userRepository::delete);
    }
}
