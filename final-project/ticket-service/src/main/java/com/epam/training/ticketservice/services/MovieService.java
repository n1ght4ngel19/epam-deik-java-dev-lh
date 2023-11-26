package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    Optional<MovieDto> createMovie(String title, String genre, int lengthInMinutes);

    Optional<MovieDto> updateMovie(String title, String genre, int lengthInMinutes);

    void deleteMovie(String title);

    Optional<MovieDto> getMovie(String title);

    List<Optional<MovieDto>> listMovies();
}
