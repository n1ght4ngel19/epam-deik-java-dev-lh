package com.epam.training.ticketservice.commands;

import com.epam.training.ticketservice.dtos.UserDto;
import com.epam.training.ticketservice.models.Room;
import com.epam.training.ticketservice.services.RoomService;
import com.epam.training.ticketservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

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
            roomService.createRoom("Girotti", 10, 10);
            roomService.createRoom("Kossuth", 10, 10);
            roomService.createRoom("Petőfi", 10, 10);
            roomService.createRoom("Kertész", 10, 10);

            return "Test dataset created successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "create room", value = "Create room")
    public String createRoom(String name, int rows, int columns) {
        try {
            return roomService.createRoom(name, rows, columns)
                    .map(roomDto -> roomDto.name() + " room created successfully")
                    .orElse("Room creation failed due to general error");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "update room", value = "Update room")
    public String updateRoom(String name, int rows, int columns) {
        try {
            return roomService.updateRoom(name, rows, columns)
                    .map(roomDto -> roomDto.name() + " room updated successfully")
                    .orElse("Room update failed due to general error");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ShellMethodAvailability("isSignedInAsAdmin")
    @ShellMethod(key = "delete room", value = "Delete room")
    public String deleteRoom(String name) {
        try {
            roomService.deleteRoom(name);

            return ("Room deleted successfully");
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

    private Availability isSignedInAsAdmin() {
        UserDto loggedInUser = userService.describe().orElse(null);

        return loggedInUser != null && loggedInUser.role().equals("admin")
                ? Availability.available()
                : Availability.unavailable("You are not signed in as admin");
    }
}
