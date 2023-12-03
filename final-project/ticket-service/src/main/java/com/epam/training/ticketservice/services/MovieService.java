package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.MovieDto;
import com.epam.training.ticketservice.exceptions.MovieAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.MovieDoesNotExistException;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    void createMovie(String title, String genre, int lengthInMinutes) throws MovieAlreadyExistsException;

    void updateMovie(String title, String genre, int lengthInMinutes) throws MovieDoesNotExistException;

    void deleteMovie(String title) throws MovieDoesNotExistException;

    List<Optional<MovieDto>> listMovies() throws MovieDoesNotExistException;
}
