package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.MovieDto;
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
    private MovieDto movieDto;

    @Override
    public List<Optional<MovieDto>> listMovies() {
        return movieRepository
                .findAll()
                .stream()
                .map(movie -> Optional.of(new MovieDto(movie.getTitle(), movie.getGenre(), movie.getLengthInMinutes())))
                .toList();
    }

    @Override
    public Optional<MovieDto> createMovie(String title, String genre, int lengthInMinutes) {
        Optional<Movie> movie = Optional.of(new Movie(title, genre, lengthInMinutes));

        movieRepository.save(movie.get());

        return Optional.of(new MovieDto(title, genre, lengthInMinutes));
    }

    @Override
    public Optional<MovieDto> getMovie(String title) {
        Optional<Movie> movie = movieRepository.findByTitle(title);

        return movie.map(movie1 -> new MovieDto(movie1.getTitle(), movie1.getGenre(), movie1.getLengthInMinutes()));
    }

    @Override
    public void updateMovie(String title, String genre, int lengthInMinutes) {
        Optional<Movie> movie = movieRepository.findByTitle(title);

        movie.ifPresent(movie1 -> {
            movie1.setTitle(title);
            movie1.setGenre(genre);
            movie1.setLengthInMinutes(lengthInMinutes);
            movieRepository.save(movie1);
        });
    }

    @Override
    public void deleteMovie(String title) {
        Optional<Movie> movie = movieRepository.findByTitle(title);

        movie.ifPresent(movieRepository::delete);
    }
}
