package com.epam.training.ticketservice.repositories;

import com.epam.training.ticketservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsernameAndPasswordAndRole(String username, String password, String role);
}
