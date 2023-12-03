package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.exceptions.UserAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.UserNotFoundException;
import com.epam.training.ticketservice.exceptions.UserNotSignedInException;
import com.epam.training.ticketservice.models.User;
import com.epam.training.ticketservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserServiceImpl underTest;
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository = mock(UserRepository.class);
        underTest = new UserServiceImpl(userRepository);
    }

    @Test
    public void testSignUpShouldCreateUserWhenUserDoesNotExist() {
        // Given
        String username = "User";
        String password = "Password";
        String role = "Role";

        // When
        underTest.signUp(username, password, role);

        // Then
        verify(userRepository, times(1)).save(new User(username, password, role));
    }

    @Test
    public void testSignUpShouldThrowUserAlreadyExistsExceptionWhenUserAlreadyExists()
            throws UserAlreadyExistsException {
        // Given
        String username = "User";
        String password = "Password";
        String role = "Role";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User(username, password, role)));

        // When and Then
        assertThrows(UserAlreadyExistsException.class, () -> underTest.signUp(username, password, role));
    }

    @Test
    public void testSignInShouldSignInUserWhenUserExists() {
        // Given
        String username = "User";
        String password = "Password";
        String role = "user";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, password, role)));
        when(userRepository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(new User(username, password, role)));

        // When
        underTest.signIn(username, password);

        // Then
        assertTrue(underTest.describe().isPresent());
        assertEquals(underTest.describe().get().name(), username);
    }

    @Test
    public void testSignInShouldThrowUserNotFoundExceptionWhenUserNotFound()
            throws UserNotFoundException {
        // Given
        String username = "User";
        String password = "Password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(UserNotFoundException.class, () -> underTest.signIn(username, password));
    }

    @Test
    public void testSignInShouldThrowIllegalArgumentExceptionWhenPasswordIsIncorrect()
            throws IllegalArgumentException {
        // Given
        String username = "User";
        String password = "Password";
        String role = "user";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User(username, password, role)));
        when(userRepository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> underTest.signIn(username, password));
    }

    @Test
    public void testAdminSignInShouldSignInAdminWhenAdminExists() {
        // Given
        String username = "admin";
        String password = "admin";
        String role = "admin";

        when(userRepository.findByUsernameAndPasswordAndRole(username, password, role))
                .thenReturn(Optional.of(new User(username, password, role)));

        // When
        underTest.adminSignIn(username, password);

        // Then
        assertTrue(underTest.describe().isPresent());
        assertEquals(underTest.describe().get().name(), username);
    }

    @Test
    public void testAdminSignInShouldThrowIllegalArgumentExceptionWhenCredentialsAreIncorrect()
            throws IllegalArgumentException {
        // Given
        String username = "admin";
        String password = "WrongPassword";
        String role = "admin";

        when(userRepository.findByUsernameAndPasswordAndRole(username, password, role))
                .thenReturn(Optional.empty());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> underTest.adminSignIn(username, password));
    }

    @Test
    public void testDescribeShouldDescribeUserWhenUserIsSignedIn() {
        // Given
        String username = "User";
        String password = "Password";
        String role = "user";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User(username, password, role)));
        when(userRepository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(new User(username, password, role)));

        // When
        underTest.signIn(username, password);

        // Then
        assertTrue(underTest.describe().isPresent());

        // When
        underTest.describe();

        // Then
        assertEquals(underTest.describe().get().name(), username);
    }

    @Test
    public void testDescribeShouldThrowUserNotSignedInExceptionWhenUserIsNotSignedIn()
            throws UserNotSignedInException {
        assertThrows(UserNotSignedInException.class, () -> underTest.describe());
    }

    @Test
    public void testIsSignedInShouldReturnFalseWhenUserIsNotSignedIn() throws UserNotSignedInException {
        assertThrows(UserNotSignedInException.class, () -> underTest.describe());
    }

    @Test
    public void testSignOutShouldThrowUserNotSignedInExceptionWhenUserIsNotSignedIn()
            throws UserNotSignedInException {
        assertThrows(UserNotSignedInException.class, () -> underTest.signOut());
    }

//    @Test
//    public void testSignOutShouldSignOutUserWhenUserIsSignedIn() {
//        // Given
//        String username = "User";
//        String password = "Password";
//        String role = "user";
//
//        when(userRepository.findByUsername(username))
//                .thenReturn(Optional.of(new User(username, password, role)));
//        when(userRepository.findByUsernameAndPassword(username, password))
//                .thenReturn(Optional.of(new User(username, password, role)));
//
//        // When
//        underTest.signIn(username, password);
//
//        // Then
//        assertTrue(underTest.describe().isPresent());
//
//        // When
//        underTest.signOut();
//
//        // Then
//        assertNull(underTest.);
//    }

    @Test
    public void testDeleteUserShouldDeleteUserWhenUserExists() {
        // Given
        String username = "User";
        String password = "Password";
        String role = "user";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User(username, password, role)));
        when(userRepository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(new User(username, password, role)));

        // When
        underTest.deleteUser(username);

        // Then
        verify(userRepository, times(1)).delete(new User(username, password, role));
    }

    @Test
    public void testDeleteUserShouldThrowUserNotFoundExceptionWhenUserDoesNotExist()
            throws UserNotFoundException {
        // Given
        String username = "admin";
        String password = "admin";
        String role = "admin";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User(username, password, role)));
        when(userRepository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(new User(username, password, role)));

        String username2 = "User";

        when(userRepository.findByUsername(username2))
                .thenReturn(Optional.empty());

        // When and Then
        assertThrows(UserNotFoundException.class, () -> underTest.deleteUser(username2));
    }

}
