package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.exceptions.CreationException;
import com.epam.training.ticketservice.models.Screening;
import com.epam.training.ticketservice.services.ScreeningService;
import com.epam.training.ticketservice.services.UserService;
import lombok.AllArgsConstructor;
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
            return screeningService.createScreening(movieTitle, roomName, start)
                    .map(screeningDto -> "Screening created successfully")
                    .orElseThrow(() -> new CreationException("Couldn't create screening"));
        } catch (CreationException e) {
            return e.getMessage();
        }
    }


    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "update screening", value = "Update screening")
    public String updateScreening(String movieTitle, String roomName, String startDateTime) {
        try {
            return screeningService.updateScreening(movieTitle, roomName, startDateTime)
                    .map(screeningDto -> "Screening updated successfully")
                    .orElse("Screening update failed");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "delete screening", value = "Delete screening")
    public String deleteScreening(String movieTitle, String roomName, String startDateTime) {
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

    private Availability isSignedInAsAdmin() {
        UserDto loggedInUser = userService.describe().orElse(null);

        return loggedInUser != null && loggedInUser.role().equals("admin")
                ? Availability.available()
                : Availability.unavailable("You are not signed in as admin");
    }
}
