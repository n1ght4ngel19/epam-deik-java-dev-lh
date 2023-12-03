package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.exceptions.MovieAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.MovieDoesNotExistException;
import com.epam.training.ticketservice.models.Movie;
import com.epam.training.ticketservice.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MovieServiceImplTest {
    private MovieServiceImpl underTest;
    private MovieRepository movieRepository;

    @BeforeEach
    public void init() {
        movieRepository = mock(MovieRepository.class);
        underTest = new MovieServiceImpl(movieRepository);
    }

    @Test
    public void testCreateMovieShouldCreateMovieWhenMovieDoesNotExist() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        // When
        underTest.createMovie(title, genre, lengthInMinutes);

        // Then
        verify(movieRepository, times(1)).save(new Movie(title, genre, lengthInMinutes));
    }

    @Test
    public void testCreateMovieShouldThrowMovieAlreadyExistsExceptionWhenMovieAlreadyExists() throws MovieAlreadyExistsException {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        when(movieRepository.findByTitle(title)).thenReturn(Optional.of(new Movie(title, genre, lengthInMinutes)));

        // When and Then
        assertThrows(MovieAlreadyExistsException.class, () -> underTest.createMovie(title, genre, lengthInMinutes));
    }

    @Test
    public void testUpdateMovieShouldUpdateMovieWhenMovieExists() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        when(movieRepository.findByTitle(title)).thenReturn(Optional.of(new Movie(title, genre, lengthInMinutes)));

        // When
        underTest.updateMovie(title, genre, lengthInMinutes);

        // Then
        verify(movieRepository, times(1)).save(new Movie(title, genre, lengthInMinutes));
    }

    @Test
    public void testUpdateMovieShouldThrowMovieAlreadyExistsExceptionWhenMovieAlreadyExists() throws MovieDoesNotExistException {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        when(movieRepository.findByTitle(title)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(MovieDoesNotExistException.class, () -> underTest.updateMovie(title, genre, lengthInMinutes));
    }

    @Test
    public void testDeleteMovieShouldDeleteMovieWhenMovieExists() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        when(movieRepository.findByTitle(title)).thenReturn(Optional.of(new Movie(title, genre, lengthInMinutes)));

        // When
        underTest.deleteMovie(title);

        // Then
        verify(movieRepository, times(1)).delete(new Movie(title, genre, lengthInMinutes));
    }

    @Test
    public void testDeleteMovieShouldThrowMovieAlreadyExistsExceptionWhenMovieAlreadyExists() throws MovieDoesNotExistException {
        // Given
        String title = "Movie";

        when(movieRepository.findByTitle(title)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(MovieDoesNotExistException.class, () -> underTest.deleteMovie(title));
    }

    @Test
    public void testListMoviesShouldListMoviesWhenMoviesExist() {
        // Given
        String title = "Movie";
        String genre = "Genre";
        int lengthInMinutes = 100;

        when(movieRepository.findAll()).thenReturn(List.of(new Movie(title, genre, lengthInMinutes)));

        // When
        underTest.listMovies();

        // Then
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testListMoviesShouldThrowMovieAlreadyExistsExceptionWhenMoviesAlreadyExists() throws MovieDoesNotExistException {
        when(movieRepository.findAll()).thenReturn(List.of());

        // When and Then
        assertThrows(MovieDoesNotExistException.class, () -> underTest.listMovies());
    }
}
