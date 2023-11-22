package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.models.Movie;
import com.epam.training.ticketservice.services.MovieService;
import com.epam.training.ticketservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class MovieCommands {
    private final MovieService movieService;
    private final UserService userService;

    @ShellMethod(key = "mk", value = "Create test dataset")
    public String mk() {
        try {
            movieService.createMovie("The Lord of the Rings", "Fantasy", 178);
            movieService.createMovie("The Lord of the Rings: The Two Towers", "Fantasy", 179);
            movieService.createMovie("The Lord of the Rings: The Return of the King", "Fantasy", 210);
            movieService.createMovie("The Hobbit: An Unexpected Journey", "Fantasy", 169);
            movieService.createMovie("The Hobbit: The Desolation of Smaug", "Fantasy", 161);
            movieService.createMovie("The Hobbit: The Battle of the Five Armies", "Fantasy", 144);
            movieService.createMovie("The Shawshank Redemption", "Drama", 142);
            movieService.createMovie("The Godfather", "Crime", 175);
            movieService.createMovie("The Godfather: Part II", "Crime", 202);
            movieService.createMovie("The Dark Knight", "Action", 152);

            return "Test dataset created successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "create movie", value = "Create movie")
    public String createMovie(String title, String genre, int lengthInMinutes) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            return movieService.createMovie(title, genre, lengthInMinutes)
                    .map(movieDto1 -> "Movie created successfully")
                    .orElse("Movie already exists");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "update movie", value = "Update movie")
    public String updateMovie(String title, String genre, int lengthInMinutes) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            return movieService.updateMovie(title, genre, lengthInMinutes)
                    .map(movieDto1 -> "Movie updated successfully")
                    .orElse("Movie doesn't exist");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "delete movie", value = "Delete movie")
    public String deleteMovie(String title) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            movieService.deleteMovie(title);

            return "Movie deleted successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "get movie", value = "Get movie")
    public String getMovie(String title) {
        try {
            return movieService.getMovie(title)
                    .map(movieDto -> new Movie(title, movieDto.genre(), movieDto.lengthInMinutes()).prettyPrint())
                    .orElse("Movie not found");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "list movies", value = "List movies")
    public String listMovies() {
        try {
            return movieService.listMovies()
                    .stream()
                    .flatMap(Optional::stream)
                    .map(movieDto ->
                            new Movie(movieDto.title(), movieDto.genre(), movieDto.lengthInMinutes())
                                    .prettyPrint())
                    .reduce(String::concat)
                    .orElse("There are no movies at the moment");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
