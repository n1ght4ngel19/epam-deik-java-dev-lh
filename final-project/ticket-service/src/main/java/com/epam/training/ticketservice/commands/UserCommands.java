package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class UserCommands {
    private final UserService userService;

    @ShellMethod(key = "sign in", value = "Sign in")
    public String signIn(String username, String password) {
        try {
            userService.signIn(username, password);

            return "Sign in successful";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "sign in privileged", value = "Admin sign in")
    public String adminSignIn(String username, String password) {
        try {
            userService.adminSignIn(username, password);

            return "Sign in successful";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "sign out", value = "Sign out")
    public String signOut() {
        try {
            userService.signOut();

            return "Sign out successful";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "describe account", value = "Describe account")
    public String describeAccount() {
        if (userService.describe().isEmpty()) {
            return "You are not signed in";
        }

        return switch (userService.describe().orElseThrow().role()) {
            case "admin" -> "Signed in with privileged account '" + userService.describe().orElseThrow().name() + "'";
            case "user" -> "Signed in with account '" + userService.describe().orElseThrow().name() + "'";
            default -> "You are not signed in";
        };
    }

    @ShellMethod(key = "exit", value = "Exit")
    public void exit() {
        System.exit(0);
    }
}
