package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.models.Screening;
import com.epam.training.ticketservice.services.ScreeningService;
import com.epam.training.ticketservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommands {
    private final ScreeningService screeningService;
    private final UserService userService;

    @ShellMethod(key = "create screening", value = "Create screening")
    public String createScreening(String movieTitle, String roomName, LocalDateTime startDateTime) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            return screeningService.createScreening(movieTitle, roomName, startDateTime)
                    .map(screeningDto -> "Screening created successfully")
                    .orElse("Screening creation failed due to general error");
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @ShellMethod(key = "update screening", value = "Update screening")
    public String updateScreening(String movieTitle, String roomName, LocalDateTime startDateTime) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            return screeningService.updateScreening(movieTitle, roomName, startDateTime)
                    .map(screeningDto -> "Screening updated successfully")
                    .orElse("Screening update failed due to general error");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "delete screening", value = "Delete screening")
    public String deleteScreening(String movieTitle, String roomName, LocalDateTime startDateTime) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            screeningService.deleteScreening(movieTitle, roomName, startDateTime);

            return "Screening deleted successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "list screenings", value = "List screenings")
    public String listScreenings() {
        try {
            return screeningService.listScreenings()
                    .stream()
                    .flatMap(Optional::stream)
                    .map(screeningDto -> Screening.fromDto(screeningDto).toString())
                    .reduce(String::concat)
                    .orElse("There are no screenings");

        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
