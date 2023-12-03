package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.RoomDto;
import com.epam.training.ticketservice.exceptions.RoomAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.RoomDoesNotExistException;
import com.epam.training.ticketservice.models.Room;
import com.epam.training.ticketservice.services.RoomService;
import com.epam.training.ticketservice.services.UserService;
import org.aspectj.apache.bcel.classfile.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RoomCommandsTest {
    @Mock
    private RoomService roomService;
    @Mock
    UserService userService;
    @InjectMocks
    private RoomCommands underTest;

    @BeforeEach
    public void init() {
        roomService = mock(RoomService.class);
        userService = mock(UserService.class);
        underTest = new RoomCommands(roomService, userService);
    }

    @Test
    public void testCreateRoomShouldCreateRoomWhenRoomDoesNotExist() {
        // Given
        String roomName = "Room";
        int numberOfRows = 10;
        int numberOfColumns = 10;

        // When
        String result = underTest.createRoom(roomName, numberOfRows, numberOfColumns);

        // Then
        assertEquals("Room created successfully", result);
        verify(roomService, times(1))
                .createRoom(roomName, numberOfRows, numberOfColumns);
    }

    @Test
    public void testCreateRoomShouldThrowRoomAlreadyExistsExceptionWhenRoomAlreadyExists() {
        // Given
        String roomName = "Room";
        int numberOfRows = 10;
        int numberOfColumns = 10;

        doThrow(new RoomAlreadyExistsException())
                .when(roomService).createRoom(roomName, numberOfRows, numberOfColumns);

        // When
        String result = underTest.createRoom(roomName, numberOfRows, numberOfColumns);

        // Then
        assertEquals("Room already exists", result);
    }

    @Test
    public void testUpdateRoomShouldUpdateRoomWhenRoomExists() {
        // Given
        String roomName = "Room";
        int numberOfRows = 10;
        int numberOfColumns = 10;

        // When
        String result = underTest.updateRoom(roomName, numberOfRows, numberOfColumns);

        // Then
        assertEquals("Room updated successfully", result);
        verify(roomService, times(1))
                .updateRoom(roomName, numberOfRows, numberOfColumns);
    }

    @Test
    public void testUpdateRoomShouldThrowRoomDoesNotExistExceptionWhenRoomDoesNotExist() {
        // Given
        String roomName = "Room";
        int numberOfRows = 10;
        int numberOfColumns = 10;

        doThrow(new RoomDoesNotExistException())
                .when(roomService).updateRoom(roomName, numberOfRows, numberOfColumns);

        // When
        String result = underTest.updateRoom(roomName, numberOfRows, numberOfColumns);

        // Then
        assertEquals("There are no rooms at the moment", result);
    }

    @Test
    public void testDeleteRoomShouldDeleteRoomWhenRoomExists() {
        // Given
        String roomName = "Room";

        // When
        String result = underTest.deleteRoom(roomName);

        // Then
        assertEquals("Room deleted successfully", result);
        verify(roomService, times(1))
                .deleteRoom(roomName);
    }

    @Test
    public void testDeleteRoomShouldThrowRoomDoesNotExistExceptionWhenRoomDoesNotExist() {
        // Given
        String roomName = "Room";

        doThrow(new RoomDoesNotExistException())
                .when(roomService).deleteRoom(roomName);

        // When
        String result = underTest.deleteRoom(roomName);

        // Then
        assertEquals("There are no rooms at the moment", result);
    }

    @Test
    public void testListRoomsShouldListRoomsWhenRoomsExist() {
        // Given
        String roomName = "Room";
        int numberOfRows = 10;
        int numberOfColumns = 10;

        Optional<RoomDto> room = Optional.of(new RoomDto(roomName, numberOfRows, numberOfColumns));

        when(roomService.listRooms()).thenReturn(List.of(room));

        // When
        String result = underTest.listRooms();

        // Then
        String expected = Room.fromDto(room.get()).toString();

        assertEquals(expected, result);
    }

    @Test
    public void testListRoomsShouldReturnThereAreNoRoomsAtTheMomentWhenRoomsDoNotExist()
            throws RoomDoesNotExistException {
        // Given
        when(roomService.listRooms())
                .thenReturn(List.of());

        // When and Then
        assertEquals("There are no rooms at the moment", underTest.listRooms());
    }
}
