package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.exceptions.MovieAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.MovieDoesNotExistException;
import com.epam.training.ticketservice.models.Movie;
import com.epam.training.ticketservice.services.MovieService;
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
public class MovieCommands {
    private final MovieService movieService;
    private final UserService userService;

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "create movie", value = "Create movie")
    public String createMovie(String title, String genre, int lengthInMinutes) {
        try {
            movieService.createMovie(title, genre, lengthInMinutes);

            return "Movie created successfully";
        } catch (MovieAlreadyExistsException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "update movie", value = "Update movie")
    public String updateMovie(String title, String genre, int lengthInMinutes) {
        try {
            movieService.updateMovie(title, genre, lengthInMinutes);

            return "Movie updated successfully";
        } catch (MovieDoesNotExistException e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "delete movie", value = "Delete movie")
    public String deleteMovie(String title) {
        try {
            movieService.deleteMovie(title);

            return "Movie deleted successfully";
        } catch (MovieDoesNotExistException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "list movies", value = "List movies")
    public String listMovies() {
        try {
            return movieService.listMovies()
                    .stream()
                    .flatMap(Optional::stream)
                    .map(movieDto -> Movie.fromDto(movieDto).toString())
                    .reduce(String::concat)
                    .orElse("There are no movies at the moment");
        } catch (Exception e) {
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
