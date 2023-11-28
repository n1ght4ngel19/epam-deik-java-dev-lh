package com.epam.training.ticketservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.epam.training.ticketservice.dtos.ScreeningDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Screenings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"room", "startTime"})
})
public class Screening {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String genre;
    private int length;
    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public Screening(String title, String genre, int length, String room, LocalDateTime startTime) {
        this.title = title;
        this.genre = genre;
        this.length = length;
        this.room = room;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(length);
    }

    @Override
    public String toString() {
        String startString = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return String.format(
                "%s (%s, %d minutes), screened in room %s, at %s\n",
                title, genre, length, room, startString);
    }

    public static Screening fromDto(ScreeningDto screeningDto) {
        return Screening.builder()
                .title(screeningDto.title())
                .genre(screeningDto.genre())
                .length(screeningDto.length())
                .room(screeningDto.room())
                .startTime(screeningDto.startTime())
                .endTime(screeningDto.endTime())
                .build();
    }

    public static ScreeningDto toDto(Screening screening) {
        return ScreeningDto.builder()
                .title(screening.title)
                .genre(screening.genre)
                .length(screening.length)
                .room(screening.room)
                .startTime(screening.startTime)
                .endTime(screening.endTime)
                .build();
    }
}
