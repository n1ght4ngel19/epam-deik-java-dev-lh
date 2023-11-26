package com.epam.training.ticketservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Screenings")
public class Screening {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String movieTitle;
    @Column(unique = true)
    private String roomName;
    private String startDateTime;

    public Screening(String movieTitle, String roomName, String startDateTime) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startDateTime = startDateTime;
    }

    public String prettyPrint() {
        return String.format(
                "%s (drama, 450 minutes), screened in room %s, at %s\n",
                movieTitle, roomName, startDateTime);
    }
}
