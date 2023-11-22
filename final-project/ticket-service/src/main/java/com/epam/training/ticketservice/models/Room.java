package com.epam.training.ticketservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
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

    public String prettyPrint() {
        return String.format("Room %s with %d seats, %d rows and %d columns\n",
                name, rows * columns, rows, columns);
    }
}
