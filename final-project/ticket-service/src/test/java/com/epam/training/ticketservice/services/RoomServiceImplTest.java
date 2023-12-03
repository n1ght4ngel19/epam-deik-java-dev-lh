package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.exceptions.RoomAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.RoomDoesNotExistException;
import com.epam.training.ticketservice.models.Room;
import com.epam.training.ticketservice.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RoomServiceImplTest {
    private RoomServiceImpl underTest;
    private RoomRepository roomRepository;

    @BeforeEach
    public void init() {
        roomRepository = mock(RoomRepository.class);
        underTest = new RoomServiceImpl(roomRepository);
    }

    @Test
    public void testCreateRoomShouldCreateRoomWhenRoomDoesNotExist() {
        // Given
        String name = "Room";
        int rows = 10;
        int columns = 10;

        // When
        underTest.createRoom(name, rows, columns);

        // Then
        verify(roomRepository, times(1)).save(new Room(name, rows, columns));
    }

    @Test
    public void testCreateRoomShouldThrowRoomAlreadyExistsExceptionWhenRoomAlreadyExists() throws RoomAlreadyExistsException {
        // Given
        String name = "Room";
        int rows = 10;
        int columns = 10;

        when(roomRepository.findByName(name)).thenReturn(Optional.of(new Room(name, rows, columns)));


        // When and Then
        assertThrows(RoomAlreadyExistsException.class, () -> underTest.createRoom(name, rows, columns));
    }

    @Test
    public void testUpdateRoomShouldUpdateRoomWhenRoomExists() {
        // Given
        String name = "Room";
        int rows = 10;
        int columns = 10;

        when(roomRepository.findByName(name)).thenReturn(Optional.of(new Room(name, rows, columns)));

        // When
        underTest.updateRoom(name, rows, columns);

        // Then
        verify(roomRepository, times(1)).save(new Room(name, rows, columns));
    }

    @Test
    public void testUpdateRoomShouldThrowRoomDoesNotExistExceptionWhenRoomDoesNotExist() throws RoomDoesNotExistException {
        // Given
        String name = "Room";
        int rows = 10;
        int columns = 10;

        when(roomRepository.findByName(name)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(RoomDoesNotExistException.class, () -> underTest.updateRoom(name, rows, columns));
    }

    @Test
    public void testDeleteRoomShouldDeleteRoomWhenRoomExists() {
        // Given
        String name = "Room";
        int rows = 10;
        int columns = 10;

        when(roomRepository.findByName(name)).thenReturn(Optional.of(new Room(name, rows, columns)));

        // When
        underTest.deleteRoom(name);

        // Then
        verify(roomRepository, times(1)).delete(new Room(name, rows, columns));
    }

    @Test
    public void testDeleteRoomShouldThrowRoomDoesNotExistExceptionWhenRoomDoesNotExist() throws RoomDoesNotExistException {
        // Given
        String name = "Room";

        when(roomRepository.findByName(name)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(RoomDoesNotExistException.class, () -> underTest.deleteRoom(name));
    }

    @Test
    public void testListRoomsShouldListRoomsWhenRoomsExist() {
        // Given
        String name = "Room";
        int rows = 10;
        int columns = 10;

        when(roomRepository.findAll()).thenReturn(List.of(new Room(name, rows, columns)));

        // When
        underTest.listRooms();

        // Then
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    public void testListRoomsShouldThrowRoomAlreadyExistsExceptionWhenRoomsDoNotExist() throws RoomDoesNotExistException {
        when(roomRepository.findAll()).thenReturn(List.of());

        // When and Then
        assertThrows(RoomDoesNotExistException.class, () -> underTest.listRooms());
    }
}
