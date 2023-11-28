package com.epam.training.ticketservice.models;

import com.epam.training.ticketservice.dtos.RoomDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private int rows;
    private int columns;

    public Room(String name, int rows, int columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public String toString() {
        return String.format("Room %s with %d seats, %d rows and %d columns\n",
                name, rows * columns, rows, columns);
    }

    public static Room fromDto(RoomDto roomDto) {
        return Room.builder()
                .name(roomDto.name())
                .rows(roomDto.rows())
                .columns(roomDto.columns())
                .build();
    }

    public static RoomDto toDto(Room room) {
        return RoomDto.builder()
                .name(room.name)
                .rows(room.rows)
                .columns(room.columns)
                .build();
    }
}
