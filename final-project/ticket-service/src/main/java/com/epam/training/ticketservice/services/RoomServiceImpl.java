package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.RoomDto;
import com.epam.training.ticketservice.exceptions.RoomAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.RoomDoesNotExistException;
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
    public void createRoom(String name, int rows, int columns) throws RoomAlreadyExistsException {
        if (roomRepository.findByName(name).isPresent()) {
            throw new RoomAlreadyExistsException();
        }

        Optional<Room> room = Optional.of(new Room(name, rows, columns));

        roomRepository.save(room.get());
    }

    @Override
    public void updateRoom(String name, int rows, int columns) throws RoomDoesNotExistException {
        Optional<Room> room = roomRepository.findByName(name);

        if (room.isEmpty()) {
            throw new RoomDoesNotExistException();
        }

        room.ifPresent(room1 -> {
            room1.setName(name);
            room1.setRows(rows);
            room1.setColumns(columns);
            roomRepository.save(room1);
        });
    }

    @Override
    public void deleteRoom(String name) throws RoomDoesNotExistException {
        Optional<Room> room = roomRepository.findByName(name);

        if (room.isEmpty()) {
            throw new RoomDoesNotExistException();
        }

        roomRepository.delete(room.get());
    }

    @Override
    public List<Optional<RoomDto>> listRooms() throws RoomDoesNotExistException {
        List<Room> rooms = roomRepository.findAll();

        if (rooms.isEmpty()) {
            throw new RoomDoesNotExistException();
        }

        return rooms.stream()
                .map(room ->
                        Optional.of(new RoomDto(room.getName(), room.getRows(), room.getColumns())))
                .toList();
    }
}
