package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    void createRoom(String name, int rows, int columns);

    void updateRoom(String name, int rows, int columns);

    List<Optional<RoomDto>> listRooms();

    void deleteRoom(String name);
}
