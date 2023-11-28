package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.ScreeningDto;
import com.epam.training.ticketservice.exceptions.ScreeningException;
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
    public Optional<ScreeningDto> createScreening(String title, String roomName, String start)
            throws ScreeningException {
        Optional<Movie> movie;
        movie = movieRepository.findByTitle(title);

        if (movie.isEmpty()) {
            throw new ScreeningException("Movie not found");
        }

        Optional<Room> room = roomRepository.findByName(roomName);

        if (room.isEmpty()) {
            throw new ScreeningException("Room not found");
        }

        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime = startDateTime.plusMinutes(movie.get().getLength());

        validateOverlap(roomName, startDateTime, endDateTime);

        Optional<Screening> screening = Optional.of(Screening.builder()
                .title(title)
                .genre(movie.get().getGenre())
                .length(movie.get().getLength())
                .room(roomName)
                .startTime(startDateTime)
                .endTime(endDateTime)
                .build());

        screeningRepository.save(screening.get());

        return Optional.of(Screening.toDto(screening.get()));
    }

    @Override
    public Optional<ScreeningDto> updateScreening(String movieTitle, String roomName, String start)
            throws ScreeningException {
        Optional<Movie> movie = movieRepository.findByTitle(movieTitle);

        if (movie.isEmpty()) {
            throw new ScreeningException("Movie not found");
        }

        Optional<Room> room = roomRepository.findByName(roomName);

        if (room.isEmpty()) {
            throw new ScreeningException("Room not found");
        }

        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime = startDateTime.plusMinutes(movie.get().getLength());

        validateOverlap(roomName, startDateTime, endDateTime);

        Optional<Screening> screening = screeningRepository.findByTitleAndRoomAndStartTime(
                movieTitle, roomName, startDateTime);

        if (screening.isEmpty()) {
            throw new ScreeningException("No screening to update");
        }

        screening.ifPresent(screening1 -> {
            screening1.setTitle(movieTitle);
            screening1.setGenre(movie.get().getGenre());
            screening1.setLength(movie.get().getLength());
            screening1.setRoom(roomName);
            screening1.setStartTime(startDateTime);
            screening1.setEndTime(endDateTime);
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

    private Optional<Screening> checkBreakAfterOverlap(String roomName, LocalDateTime startDateTime) {
        return screeningRepository.findByRoom(roomName)
                .stream()
                .flatMap(Optional::stream)
                .filter(scr -> startDateTime.isAfter(scr.getEndTime())
                        && startDateTime.isBefore(scr.getEndTime().plusMinutes(10)))
                .findFirst();
    }

    private Optional<Screening> checkBreakBeforeOverlap(String roomName, LocalDateTime endDateTime) {
        return screeningRepository.findByRoom(roomName)
                .stream()
                .flatMap(Optional::stream)
                .filter(scr -> endDateTime.isAfter(scr.getStartTime().minusMinutes(10))
                        && endDateTime.isBefore(scr.getStartTime().plusMinutes(1)))
                .findFirst();
    }

    private Optional<Screening> checkOverlap(String roomName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return screeningRepository.findByRoom(roomName)
                .stream()
                .flatMap(Optional::stream)
                .filter(scr -> (startDateTime.isAfter(scr.getStartTime())
                        && startDateTime.isBefore(scr.getEndTime()))
                        || (startDateTime.isBefore(scr.getStartTime())
                        && endDateTime.isAfter(scr.getStartTime()))
                        || (startDateTime.isEqual(scr.getStartTime())
                        || startDateTime.isEqual(scr.getEndTime()))
                )
                .findFirst();
    }

    private void validateOverlap(String roomName, LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws ScreeningException {
        Optional<Screening> overlap = checkOverlap(roomName, startDateTime, endDateTime);

        if (overlap.isPresent()) {
            throw new ScreeningException("There is an overlapping screening");
        }

        Optional<Screening> breakAfterOverlap = checkBreakAfterOverlap(roomName, startDateTime);

        if (breakAfterOverlap.isPresent()) {
            throw new ScreeningException(
                    "This would start in the break period after another screening in this room"
            );
        }

        Optional<Screening> breakBeforeOverlap = checkBreakBeforeOverlap(roomName, endDateTime);

        if (breakBeforeOverlap.isPresent()) {
            throw new ScreeningException(
                    "This would not grant a 10 minute break period before another screening in this room"
            );
        }
    }
}
