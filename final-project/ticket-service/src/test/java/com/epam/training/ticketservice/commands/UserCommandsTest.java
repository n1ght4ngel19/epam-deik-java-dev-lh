package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.exceptions.UserNotFoundException;
import com.epam.training.ticketservice.exceptions.UserNotSignedInException;
import com.epam.training.ticketservice.models.User;
import com.epam.training.ticketservice.repositories.UserRepository;
import com.epam.training.ticketservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserCommandsTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserCommands underTest;

    @BeforeEach
    public void init() {
        userService = mock(UserService.class);
        underTest = new UserCommands(userService);
    }

//    @Test
//    public void testSignInSuccessful() {
//        // Given
//        String username = "User";
//        String password = "Password";
//
//        doReturn("Sign in successful")
//                .when(userService).signIn(username, password);
//
//        // When
//        String result = underTest.signIn(username, password);
//
//        // Then
//        assertEquals("Sign in successful", result);
//        verify(userService, times(1)).signIn(username, password);
//    }

    @Test
    public void testSignInShouldReturnErrorMessageWhenUserNotFound() {
        // Given
        String username = "User";
        String password = "Password";

        doThrow(new UserNotFoundException())
                .when(userService).signIn(username, password);

        // When
        String result = underTest.signIn(username, password);

        // Then
        assertEquals("User not found", result);
    }

    @Test
    public void testSignInShouldReturnErrorMessageWhenUserNotSignedIn() {
        // Given
        String username = "User";
        String password = "Password";

        doThrow(new UserNotSignedInException())
                .when(userService).signIn(username, password);

        // When
        String result = underTest.signIn(username, password);

        // Then
        assertEquals("You are not signed in", result);
    }

//    @Test
//    public void testSignUpShouldReturnSuccessMessageWhenSignUpIsSuccessful() {
//        // Given
//        String username = "User";
//        String password = "Password";
//        String role = "Role";
//
//        doReturn("Sign up successful")
//                .when(userService).signUp(username, password, role);
//
//        // When
//        String result = underTest.signUp(username, password, role);
//
//        // Then
//        assertEquals("Sign up successful", result);
//        verify(userService, times(1)).signUp(username, password, role);
//    }

//    @Test
//    public void testSignUpShouldReturnErrorMessageWhenSignUpIsUnsuccessful() {
//        // Given
//        String username = "User";
//        String password = "Password";
//        String role = "Role";
//
//        doThrow(new RuntimeException())
//                .when(userService).signUp(username, password, role);
//
//        // When
//        String result = underTest.signUp(username, password, role);
//
//        // Then
//        assertEquals("Sign up failed due to general error", result);
//    }

//    @Test
//    public void testDescribeAccountShouldReturnSuccessMessageWhenDescribeAccountIsSuccessful() {
//        // Given
//        String username = "User";
//        String password = "Password";
//        String role = "user";
//
//        doReturn(Optional.of(new UserDto(username, password, role)))
//                .when(userService).describe();
//
//        // When
//        String result = underTest.describeAccount();
//
//        // Then
//        assertEquals("Signed in with account '" + username + "'", result);
//        verify(userService, times(1)).describe();
//    }

//    @Test
//    public void testDescribeAccountShouldReturnErrorMessageWhenUserNotSignedIn() {
//        // Given
//        doThrow(new UserNotSignedInException())
//                .when(userService).describe();
//
//        // When
//        String result = underTest.describeAccount();
//
//        // Then
//        assertEquals("You are not signed in", result);
//    }

//    @Test
//    public void testAdminSignInSuccessful() {
//        // Given
//        String username = "admin";
//        String password = "admin";
//
//        doReturn("Sign in successful")
//                .when(userService).adminSignIn(username, password);
//
//        // When
//        String result = underTest.adminSignIn(username, password);
//
//        // Then
//        assertEquals("Sign in successful", result);
//        verify(userService, times(1)).adminSignIn(username, password);
//    }

    @Test
    public void testAdminSignInShouldReturnErrorMessageWhenUserNotFound() {
        // Given
        String username = "admin";
        String password = "admin";

        doThrow(new UserNotFoundException())
                .when(userService).adminSignIn(username, password);

        // When
        String result = underTest.adminSignIn(username, password);

        // Then
        assertEquals("User not found", result);
    }

    @Test
    public void testAdminSignInShouldReturnErrorMessageWhenUserNotSignedIn() {
        // Given
        String username = "admin";
        String password = "admin";

        doThrow(new UserNotSignedInException())
                .when(userService).adminSignIn(username, password);

        // When
        String result = underTest.adminSignIn(username, password);

        // Then
        assertEquals("You are not signed in", result);
    }


//    @Test
//    public void testSignInShouldReturnSuccessMessageWhenSignInIsSuccessful() {
//        // Given
//        String username = "admin";
//        String password = "admin";
//
//        when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());
//
//        // When and Then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.signIn(username, password));
//
//        assertEquals("Login failed due to incorrect credentials", exception.getMessage());
//    }

//    @Test
//    public void testSignInShouldThrowRuntimeExceptionWhenSignInIsUnsuccessful() {
//        // Given
//        String username = "admin";
//        String password = "WrongPassword";
//
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(username, password, "admin")));
//
//
//    }

    @Test
    public void testSignOutShouldReturnSuccessMessageWhenSignOutIsSuccessful() {
        // Given
        // When
        String result = underTest.signOut();

        // Then
        assertEquals("Sign out successful", result);
        verify(userService, times(1)).signOut();
    }

//    @Test
//    public void testDescribeAccountShouldReturnSuccessMessageWhenDescribeAccountIsSuccessful() {
//        // Given
//        String username = "admin";
//        String password = "admin";
//        String role = "admin";
//
//        when(userService.describe()).thenReturn(Optional.of(new UserDto(username, password, role)));
//
//        // When
//        String result = underTest.describeAccount();
//
//        // Then
//        assertEquals("Signed in with privileged account 'admin'", result);
//        verify(userService, times(1)).describe();
//    }

//    @Test
//    public void testSignOutShouldReturnErrorMessageWhenSignOutIsUnsuccessful() {
//        // Given
//        doThrow(new RuntimeException())
//                .when(userService).signOut();
//
//        // When
//        String result = underTest.signOut();
//
//        // Then
//        assertEquals("You are not signed in", result);
//    }
}
