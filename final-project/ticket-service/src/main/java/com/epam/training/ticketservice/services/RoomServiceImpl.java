package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.RoomDto;
import com.epam.training.ticketservice.models.Room;
import com.epam.training.ticketservice.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public List<Optional<RoomDto>> listRooms() {
        return roomRepository
                .findAll()
                .stream()
                .map(room ->
                        Optional.of(new RoomDto(room.getName(), room.getRows(), room.getColumns())))
                .toList();
    }

    @Override
    public Optional<RoomDto> createRoom(String name, int rows, int columns) {
        Optional<Room> room = Optional.of(new Room(name, rows, columns));

        roomRepository.save(room.get());

        return Optional.of(new RoomDto(name, rows, columns));
    }

    @Override
    public Optional<RoomDto> getRoom(String name) {
        Optional<Room> room = roomRepository.findByName(name);

        return room.map(room1 ->
                new RoomDto(room1.getName(), room1.getRows(), room1.getColumns()));
    }


    @Override
    public Optional<RoomDto> updateRoom(String name, int rows, int columns) {
        Optional<Room> room = roomRepository.findByName(name);

        room.ifPresent(room1 -> {
            room1.setName(name);
            room1.setRows(rows);
            room1.setColumns(columns);
            roomRepository.save(room1);
        });

        return room.map(room1 ->
                new RoomDto(room1.getName(), room1.getRows(), room1.getColumns()));
    }

    @Override
    public void deleteRoom(String name) {
        Optional<Room> room = roomRepository.findByName(name);

        room.ifPresent(roomRepository::delete);
    }
}
