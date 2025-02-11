package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.models.Screening;
import com.epam.training.ticketservice.services.ScreeningService;
import com.epam.training.ticketservice.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommands {
    private final ScreeningService screeningService;
    private final UserService userService;

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "create screening", value = "Create screening")
    public String createScreening(String movieTitle, String roomName, String start) {
        try {
            screeningService.createScreening(movieTitle, roomName, start);
            return "Screening created successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "update screening", value = "Update screening")
    public String updateScreening(String movieTitle, String roomName, String startDateTime) {
        try {
            screeningService.updateScreening(movieTitle, roomName, startDateTime);

            return "Screening updated successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "delete screening", value = "Delete screening")
    public String deleteScreening(String movieTitle, String roomName, String startDateTime) {
        try {
            screeningService.deleteScreening(movieTitle, roomName, startDateTime);

            return "Screening deleted successfully";
        } catch (RuntimeException e) {
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
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @Generated
    private Availability isSignedInAsAdmin() {
        UserDto loggedInUser = userService.describe().orElse(null);

        return loggedInUser != null && loggedInUser.role().equals("admin")
                ? Availability.available()
                : Availability.unavailable("You are not signed in as admin");
    }
}
