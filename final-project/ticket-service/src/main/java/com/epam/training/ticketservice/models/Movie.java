package com.epam.training.ticketservice.models;

import com.epam.training.ticketservice.dtos.MovieDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Movies")
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String title;
    private String genre;
    private int length;

    public Movie(String title, String genre, int lengthInMinutes) {
        this.title = title;
        this.genre = genre;
        this.length = lengthInMinutes;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s minutes)\n", title, genre, length);
    }

    public static Movie fromDto(MovieDto movieDto) {
        return Movie.builder()
                .title(movieDto.title())
                .genre(movieDto.genre())
                .length(movieDto.length())
                .build();
    }

    public static MovieDto toDto(Movie movie) {
        return MovieDto.builder()
                .title(movie.title)
                .genre(movie.genre)
                .length(movie.length)
                .build();
    }
}
