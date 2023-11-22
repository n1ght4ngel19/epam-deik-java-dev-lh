package com.epam.training.ticketservice.db;

import com.epam.training.ticketservice.models.User;
import com.epam.training.ticketservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        userRepository.save(new User("admin", "admin", "admin"));
    }
}
