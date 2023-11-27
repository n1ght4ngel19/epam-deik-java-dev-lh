package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.ScreeningDto;
import com.epam.training.ticketservice.models.Movie;
import com.epam.training.ticketservice.models.Screening;
import com.epam.training.ticketservice.repositories.MovieRepository;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;

    @Override
    public Optional<ScreeningDto> createScreening(String title, String room, LocalDateTime startTime) {
        Optional<Movie> movie = movieRepository.findByTitle(title);

        if (movie.isEmpty()) {
            return Optional.empty();
        }


        Optional<Screening> screening = Optional.of(Screening.builder()
                .title(title)
                .genre(movie.get().getGenre())
                .length(movie.get().getLength())
                .room(room)
                .startTime(startTime)
                .build());

        screeningRepository.save(screening.get());

        return Optional.of(Screening.toDto(screening.get()));
    }

    @Override
    public Optional<ScreeningDto> updateScreening(String movieTitle, String roomName, LocalDateTime startDateTime) {
        Optional<Screening> screening = screeningRepository.findByTitleAndRoomAndStartTime(
                movieTitle, roomName, startDateTime);

        screening.ifPresent(screening1 -> {
            screening1.setTitle(movieTitle);
            screening1.setRoom(roomName);
            screening1.setStartTime(startDateTime);
            screeningRepository.save(screening1);
        });

        return screening.map(Screening::toDto);
    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime startDateTime) {
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
