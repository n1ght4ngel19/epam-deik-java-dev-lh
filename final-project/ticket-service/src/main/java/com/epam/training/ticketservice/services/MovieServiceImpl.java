package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.MovieDto;
import com.epam.training.ticketservice.exceptions.MovieAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.MovieDoesNotExistException;
import com.epam.training.ticketservice.models.Movie;
import com.epam.training.ticketservice.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    public void createMovie(String title, String genre, int lengthInMinutes) throws MovieAlreadyExistsException {
        if (movieRepository.findByTitle(title).isPresent()) {
            throw new MovieAlreadyExistsException();
        }

        Optional<Movie> movie = Optional.of(new Movie(title, genre, lengthInMinutes));

        movieRepository.save(movie.get());
    }

    @Override
    public void updateMovie(String title, String genre, int lengthInMinutes) throws MovieDoesNotExistException {
        Optional<Movie> movie = movieRepository.findByTitle(title);

        if (movie.isEmpty()) {
            throw new MovieDoesNotExistException();
        }

        movie.ifPresent(movie1 -> {
            movie1.setTitle(title);
            movie1.setGenre(genre);
            movie1.setLength(lengthInMinutes);
            movieRepository.save(movie1);
        });
    }

    @Override
    public void deleteMovie(String title) throws MovieDoesNotExistException {
        Optional<Movie> movie = movieRepository.findByTitle(title);

        if (movie.isEmpty()) {
            throw new MovieDoesNotExistException();
        }

        movieRepository.delete(movie.get());
    }

    @Override
    public List<Optional<MovieDto>> listMovies() throws MovieDoesNotExistException {
        List<Movie> movies = movieRepository.findAll();

        if (movies.isEmpty()) {
            throw new MovieDoesNotExistException();
        }

        return movies.stream()
                .map(movie ->
                        Optional.of(new MovieDto(movie.getTitle(), movie.getGenre(), movie.getLength())))
                .toList();
    }
}
