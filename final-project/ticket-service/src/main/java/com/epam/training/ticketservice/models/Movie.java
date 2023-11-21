package com.epam.training.ticketservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.MessageFormat;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Movies")
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String title;
    private String genre;
    private int lengthInMinutes;

    public Movie(String title, String genre, int lengthInMinutes) {
        this.title = title;
        this.genre = genre;
        this.lengthInMinutes = lengthInMinutes;
    }

    public String prettyPrint() {
        return MessageFormat.format("{0} ({1}, {2} minutes)\n", title, genre, lengthInMinutes);
    }
}
