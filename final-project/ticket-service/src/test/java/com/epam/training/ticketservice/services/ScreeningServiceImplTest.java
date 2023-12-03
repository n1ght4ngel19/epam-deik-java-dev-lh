package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.exceptions.MovieDoesNotExistException;
import com.epam.training.ticketservice.exceptions.RoomDoesNotExistException;
import com.epam.training.ticketservice.exceptions.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.exceptions.ScreeningOverlapException;
import com.epam.training.ticketservice.models.Movie;
import com.epam.training.ticketservice.models.Room;
import com.epam.training.ticketservice.models.Screening;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ScreeningServiceImplTest {
    private ScreeningRepository screeningRepository;
    private MovieRepository movieRepository;
    private RoomRepository roomRepository;
    private ScreeningServiceImpl underTest;
    private MovieServiceImpl movieService;

    @BeforeEach
    public void init() {
        screeningRepository = mock(ScreeningRepository.class);
        movieRepository = mock(MovieRepository.class);
        roomRepository = mock(RoomRepository.class);
        underTest = new ScreeningServiceImpl(screeningRepository, movieRepository, roomRepository);
        movieService = new MovieServiceImpl(movieRepository);
    }

    @Test
    public void testCreateScreeningShouldThrowMovieDoesNotExistExceptionWhenMovieDoesNotExist()
            throws MovieDoesNotExistException {
        // Given
        String title = "Movie";
        String roomName = "Room";
        String start = "2021-04-01 10:00";

        // When and Then
        assertThrows(MovieDoesNotExistException.class, () ->
                underTest.createScreening(title, roomName, start));
    }

    @Test
    public void testCreateScreeningShouldThrowRoomDoesNotExistExceptionWhenRoomDoesNotExist()
            throws MovieDoesNotExistException {
        // Given
        String title = "Movie";
        String roomName = "Room";
        String start = "2021-04-01 10:00";

        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(new Movie(title, "Genre", 100)));


        // When and Then
        assertThrows(RoomDoesNotExistException.class, () ->
                underTest.createScreening(title, roomName, start));
    }

    @Test
    public void testCreateScreeningShouldCreateScreeningWhenScreeningDoesNotExist()
            throws MovieDoesNotExistException, RoomDoesNotExistException, ScreeningOverlapException {
        // Given
        String title = "Movie";
        String roomName = "Room";
        String start = "2021-04-01 10:00";
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(new Movie(title, "Genre", 100)));

        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.of(new Room(roomName, 10, 10)));

        when(screeningRepository.findByTitleAndRoomAndStartTime(title, roomName, startDateTime))
                .thenReturn(Optional.empty());

        // When
        underTest.createScreening(title, roomName, start);

        // Then
        verify(screeningRepository, times(1))
                .save(new Screening(title, "Genre", 100, roomName, startDateTime));
    }

    @Test
    public void testUpdateScreeningShouldThrowMovieDoesNotExistExceptionWhenMovieDoesNotExist()
            throws RoomDoesNotExistException, ScreeningOverlapException {
        // Given
        String title = "Movie";
        String roomName = "Room";
        String start = "2021-04-01 10:00";

        // When and Then
        assertThrows(MovieDoesNotExistException.class, () ->
                underTest.updateScreening(title, roomName, start));
    }

    @Test
    public void testUpdateScreeningShouldThrowRoomDoesNotExistExceptionWhenRoomDoesNotExist()
            throws MovieDoesNotExistException {
        // Given
        String title = "Movie";
        String roomName = "Room";
        String start = "2021-04-01 10:00";

        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(new Movie(title, "Genre", 100)));

        // When and Then
        assertThrows(RoomDoesNotExistException.class, () ->
                underTest.updateScreening(title, roomName, start));
    }

    @Test
    public void testUpdateScreeningShouldThrowScreeningDoesNotExistExceptionWhenScreeningDoesNotExist()
            throws MovieDoesNotExistException, RoomDoesNotExistException, ScreeningOverlapException {
        // Given
        String title = "Movie";
        String roomName = "Room";
        String start = "2021-04-01 10:00";
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(new Movie(title, "Genre", 100)));

        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.of(new Room(roomName, 10, 10)));

        when(screeningRepository.findByTitleAndRoomAndStartTime(title, roomName, startDateTime))
                .thenReturn(Optional.empty());

        // When and Then
        assertThrows(ScreeningDoesNotExistException.class, () ->
                underTest.updateScreening(title, roomName, start));
    }

    @Test
    public void testDeleteScreeningShouldThrowScreeningDoesNotExistExceptionWhenScreeningDoesNotExist()
            throws ScreeningDoesNotExistException {
        // Given
        String title = "Movie";
        String roomName = "Room";
        String start = "2021-04-01 10:00";
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(new Movie(title, "Genre", 100)));

        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.of(new Room(roomName, 10, 10)));

        when(screeningRepository.findByTitleAndRoomAndStartTime(title, roomName, startDateTime))
                .thenReturn(Optional.empty());

        // When and Then
        assertThrows(ScreeningDoesNotExistException.class, () ->
                underTest.deleteScreening(title, roomName, start));
    }

    @Test
    public void testDeleteScreeningShouldDeleteScreeningWhenScreeningExists() {
        // Given
        String title = "Movie";
        String roomName = "Room";
        String start = "2021-04-01 10:00";
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(new Movie(title, "Genre", 100)));

        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.of(new Room(roomName, 10, 10)));

        when(screeningRepository.findByTitleAndRoomAndStartTime(title, roomName, startDateTime))
                .thenReturn(Optional.of(new Screening(title, "Genre", 100, roomName, startDateTime)));

        // When
        underTest.deleteScreening(title, roomName, start);

        // Then
        verify(screeningRepository, times(1))
                .delete(new Screening(title, "Genre", 100, roomName, startDateTime));
    }

//    @Test
//    public void testCreateScreeningShouldCreateScreeningWhenScreeningDoesNotExist() {
//        // Given
//        String title = "Movie";
//        String genre = "Genre";
//        int lengthInMinutes = 100;
//
//        movieService.createMovie(title, genre, lengthInMinutes);
//
//        verify(movieRepository, times(1)).save(new Movie(title, genre, lengthInMinutes));
//
//
//        String roomName = "Room";
//        String start = "2021-04-01 10:00";
//        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//
//        // When
//        underTest.createScreening(title, roomName, start);
//
//        // Then
//        verify(screeningRepository, times(1))
//                .save(new Screening(title, "Genre", 100, roomName, startDateTime));
//    }

//    @Test
//    public void testCreateScreeningShouldThrowScreeningAlreadyExistsExceptionWhenScreeningAlreadyExists() {
//        // Given
//        String title = "Movie";
//        String genre = "Genre";
//        int lengthInMinutes = 100;
//
//        movieService.createMovie(title, genre, lengthInMinutes);
//
//        verify(movieRepository, times(1)).save(new Movie(title, genre, lengthInMinutes));
//
//
//        String roomName = "Room";
//        String start = "2021-04-01 10:00";
//        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//
//        when(screeningRepository
//                .findByTitleAndRoomAndStartTime(title, roomName, startDateTime))
//                .thenThrow(ScreeningAlreadyExistsException.class);
//
//        // When and Then
//        assertThrows(ScreeningAlreadyExistsException.class, () ->
//                underTest.createScreening(title, roomName, start));
//    }
}
