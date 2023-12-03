package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.MovieDto;
import com.epam.training.ticketservice.exceptions.MovieAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.MovieDoesNotExistException;
import com.epam.training.ticketservice.models.Movie;
import com.epam.training.ticketservice.services.MovieService;
import com.epam.training.ticketservice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MovieCommandsTest {
    @Mock
    private MovieService movieService;
    @Mock
    UserService userService;
    @InjectMocks
    private MovieCommands underTest;

    @BeforeEach
    public void init() {
        movieService = mock(MovieService.class);
        userService = mock(UserService.class);
        underTest = new MovieCommands(movieService, userService);
    }

    @Test
    public void testCreateMovieShouldCreateMovieWhenMovieDoesNotExist() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        // When
        String result = underTest.createMovie(title, genre, lengthInMinutes);

        // Then
        assertEquals("Movie created successfully", result);
        verify(movieService, times(1))
                .createMovie(title, genre, lengthInMinutes);
    }

    @Test
    public void testCreateMovieShouldThrowMovieAlreadyExistsExceptionWhenMovieAlreadyExists() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        doThrow(new MovieAlreadyExistsException())
                .when(movieService).createMovie(title, genre, lengthInMinutes);

        // When
        String result = underTest.createMovie(title, genre, lengthInMinutes);

        // Then
        assertEquals("Movie already exists", result);
        verify(movieService, times(1))
                .createMovie(title, genre, lengthInMinutes);
    }

    @Test
    public void testUpdateMovieShouldUpdateMovieWhenMovieExists() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        // When
        String result = underTest.updateMovie(title, genre, lengthInMinutes);

        // Then
        assertEquals("Movie updated successfully", result);
        verify(movieService, times(1))
                .updateMovie(title, genre, lengthInMinutes);
    }

    @Test
    public void testUpdateMovieShouldThrowMovieDoesNotExistExceptionWhenMovieDoesNotExist() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        doThrow(new MovieDoesNotExistException())
                .when(movieService).updateMovie(title, genre, lengthInMinutes);

        // When
        String result = underTest.updateMovie(title, genre, lengthInMinutes);

        // Then
        assertEquals("There are no movies at the moment", result);
        verify(movieService, times(1))
                .updateMovie(title, genre, lengthInMinutes);
    }

    @Test
    public void testDeleteMovieShouldDeleteMovieWhenMovieExists() {
        // Given
        String title = "Movie";

        // When
        String result = underTest.deleteMovie(title);

        // Then
        assertEquals("Movie deleted successfully", result);
        verify(movieService, times(1))
                .deleteMovie(title);
    }

    @Test
    public void testDeleteMovieShouldThrowMovieDoesNotExistExceptionWhenMovieDoesNotExist() {
        // Given
        String title = "Movie";

        doThrow(new MovieDoesNotExistException())
                .when(movieService).deleteMovie(title);

        // When
        String result = underTest.deleteMovie(title);

        // Then
        assertEquals("There are no movies at the moment", result);
        verify(movieService, times(1))
                .deleteMovie(title);
    }

    @Test
    public void testListMoviesShouldListMoviesWhenMoviesExist() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        Optional<MovieDto> movie = Optional.of(new MovieDto(title, genre, lengthInMinutes));

        when(movieService.listMovies()).thenReturn(List.of(movie));

        // When
        String result = underTest.listMovies();

        // Then
        String expected = Movie.fromDto(movie.get()).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testListMoviesShouldReturnThereAreNoMoviesAtTheMomentWhenMoviesDoNotExist() {
        // Given
        when(movieService.listMovies())
                .thenReturn(List.of());

        // When
        String result = underTest.listMovies();

        // Then
        assertEquals("There are no movies at the moment", result);
    }

    @Test
    public void testListMoviesShouldThrowMovieDoesNotExistExceptionWhenMoviesDoNotExist()
            throws MovieDoesNotExistException {
        // Given
        when(movieService.listMovies())
                .thenReturn(List.of());

        // When and Then
        assertEquals("There are no movies at the moment", underTest.listMovies());
    }
}
