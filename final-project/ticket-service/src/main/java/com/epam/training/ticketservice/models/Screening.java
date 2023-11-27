package com.epam.training.ticketservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.epam.training.ticketservice.dtos.ScreeningDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Screenings")
public class Screening {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String title;
    private String genre;
    private int length;
    private String room;
    private LocalDateTime startTime;

    public Screening(String title, String genre, int length, String room, LocalDateTime startTime) {
        this.title = title;
        this.room = room;
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return String.format(
                "%s (%s, %d minutes), screened in room %s, at %s\n",
                title, genre, length, room, startTime.toString());
    }

    public static Screening fromDto(ScreeningDto screeningDto) {
        return Screening.builder()
                .title(screeningDto.title())
                .genre(screeningDto.genre())
                .length(screeningDto.length())
                .room(screeningDto.room())
                .startTime(screeningDto.startTime())
                .build();
    }

    public static ScreeningDto toDto(Screening screening) {
        return ScreeningDto.builder()
                .title(screening.title)
                .genre(screening.genre)
                .length(screening.length)
                .room(screening.room)
                .startTime(screening.startTime)
                .build();
    }
}
