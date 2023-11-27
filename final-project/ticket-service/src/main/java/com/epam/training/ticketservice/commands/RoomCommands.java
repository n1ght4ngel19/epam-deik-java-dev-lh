package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.models.Room;
import com.epam.training.ticketservice.services.RoomService;
import com.epam.training.ticketservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class RoomCommands {
    private final RoomService roomService;
    private final UserService userService;

    @ShellMethod(key = "mr", value = "Create test room dataset")
    public String mr() {
        try {
            roomService.createRoom("Pedersoli", 10, 10);
            roomService.createRoom("Bud Spencer", 10, 10);
            roomService.createRoom("Terence Hill", 10, 10);
            roomService.createRoom("Havasi", 10, 10);
            roomService.createRoom("Kossuth", 10, 10);
            roomService.createRoom("Petőfi", 10, 10);
            roomService.createRoom("Kertész", 10, 10);

            return "Test dataset created successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "create room", value = "Create room")
    public String createRoom(String name, int rows, int columns) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            return roomService.createRoom(name, rows, columns)
                    .map(roomDto -> roomDto.name() + " room created successfully")
                    .orElse("Room creation failed due to general error");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "update room", value = "Update room")
    public String updateRoom(String name, int rows, int columns) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            return roomService.updateRoom(name, rows, columns)
                    .map(roomDto -> roomDto.name() + " room updated successfully")
                    .orElse("Room update failed due to general error");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "delete room", value = "Delete room")
    public String deleteRoom(String name) {
        UserDto loggedInUser = userService.describe().orElse(null);

        if (loggedInUser == null || !loggedInUser.role().equals("admin")) {
            return "You are not signed in as admin";
        }

        try {
            roomService.deleteRoom(name);

            return ("Room deleted successfully");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "get room", value = "Get room")
    public String getRoom(String name) {
        try {
            return roomService.getRoom(name)
                    .map(roomDto -> Room.fromDto(roomDto).toString())
                    .orElse("Room doesn't exist");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "list rooms", value = "List rooms")
    public String listRooms() {
        try {
            return roomService.listRooms()
                    .stream()
                    .flatMap(Optional::stream)
                    .map(roomDto -> Room.fromDto(roomDto).toString())
                    .reduce(String::concat)
                    .orElse("There are no rooms at the moment");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
