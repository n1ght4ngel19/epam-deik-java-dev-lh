package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.ScreeningDto;
import com.epam.training.ticketservice.exceptions.CreationException;
import com.epam.training.ticketservice.models.Movie;
import com.epam.training.ticketservice.models.Room;
import com.epam.training.ticketservice.models.Screening;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.RoomRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Override
    public Optional<ScreeningDto> createScreening(String title, String roomName, String start) throws CreationException {
        Optional<Movie> movie = movieRepository.findByTitle(title);

        if (movie.isEmpty()) {
            throw new CreationException("Movie not found");
        }

        Optional<Room> room = roomRepository.findByName(roomName);

        if (room.isEmpty()) {
            throw new CreationException("Room not found");
        }

        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime = startDateTime.plusMinutes(movie.get().getLength());
        LocalDateTime breakEndDateTime = endDateTime.plusMinutes(10);

        Optional<Screening> overlap =
                screeningRepository.findByRoom(roomName)
                        .stream()
                        .flatMap(Optional::stream)
                        .filter(scr -> startDateTime.isAfter(scr.getStartTime()) &&
                                startDateTime.isBefore(scr.getEndTime()))
                        .findFirst();

        if (overlap.isPresent()) {
            throw new CreationException("There is an overlapping screening");
        }

        Optional<Screening> breakOverlap =
                screeningRepository.findByRoomAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual
                        (roomName, endDateTime, breakEndDateTime);

        if (breakOverlap.isPresent()) {
            throw new CreationException("This would start in the break period after another screening in this room");
        }

        Optional<Screening> screening = Optional.of(Screening.builder()
                .title(title)
                .genre(movie.get().getGenre())
                .length(movie.get().getLength())
                .room(roomName)
                .startTime(startDateTime)
                .build());

        screeningRepository.save(screening.get());

        return Optional.of(Screening.toDto(screening.get()));
    }

    @Override
    public Optional<ScreeningDto> updateScreening(String movieTitle, String roomName, String start) {
        Optional<Movie> movie = movieRepository.findByTitle(movieTitle);

        if (movie.isEmpty()) {
            // No movie found in database
            return Optional.empty();
        }

        Optional<Room> room = roomRepository.findByName(roomName);

        if (room.isEmpty()) {
            // No room found in database
            return Optional.empty();
        }

        LocalDateTime startDateTime =
                LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime =
                startDateTime.plusMinutes(movie.get().getLength());
        LocalDateTime breakEndDateTime =
                endDateTime.plusMinutes(10);

        if (screeningRepository.existsByRoomAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual
                (roomName, startDateTime, endDateTime)) {
            System.out.println("There is an overlapping screening");

            return Optional.empty();
        }

        if (screeningRepository.existsByRoomAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual
                (roomName, endDateTime, breakEndDateTime)) {
            System.out.println("This would start in the break period after another screening in this room");

            return Optional.empty();
        }

        Optional<Screening> screening = screeningRepository.findByTitleAndRoomAndStartTime(
                movieTitle, roomName, startDateTime);

        if (screening.isEmpty()) {
            // No screening found in database
            return Optional.empty();
        }

        screening.ifPresent(screening1 -> {
            screening1.setTitle(movieTitle);
            screening1.setRoom(roomName);
            screening1.setStartTime(startDateTime);
            screeningRepository.save(screening1);
        });

        return screening.map(Screening::toDto);
    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, String start) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Optional<Screening> screening = screeningRepository.findByTitleAndRoomAndStartTime(
                movieTitle, roomName, startDateTime);

        screening.ifPresent(screeningRepository::delete);
    }

    @Override
    public List<Optional<ScreeningDto>> listScreenings() {
        return screeningRepository
                .findAll()
                .stream()
                .map(screening -> Optional.of(Screening.toDto(screening)))
                .toList();
    }
}
