package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.ScreeningDto;
import com.epam.training.ticketservice.models.Screening;
import com.epam.training.ticketservice.repositories.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {
    private final ScreeningRepository screeningRepository;

    @Override
    public Optional<ScreeningDto> createScreening(String movieTitle, String roomName, String startDateTime) {
        Optional<Screening> screening = Optional.of(new Screening(movieTitle, roomName, startDateTime));

        screeningRepository.save(screening.get());

        return Optional.of(new ScreeningDto(movieTitle, roomName, startDateTime));
    }

    @Override
    public Optional<ScreeningDto> updateScreening(String movieTitle, String roomName, String startDateTime) {
        Optional<Screening> screening = screeningRepository.findByMovieTitleAndRoomNameAndStartDateTime(
                movieTitle, roomName, startDateTime);

        screening.ifPresent(screening1 -> {
            screening1.setMovieTitle(movieTitle);
            screening1.setRoomName(roomName);
            screening1.setStartDateTime(startDateTime);
            screeningRepository.save(screening1);
        });

        return screening.map(screening1 ->
                new ScreeningDto(screening1.getMovieTitle(), screening1.getRoomName(), screening1.getStartDateTime()));
    }

    @Override
    public void deleteScreening(String movieTitle, String roomName, String startDateTime) {
        Optional<Screening> screening = screeningRepository.findByMovieTitleAndRoomNameAndStartDateTime(
                movieTitle, roomName, startDateTime);

        screening.ifPresent(screeningRepository::delete);
    }

    @Override
    public List<Optional<ScreeningDto>> listScreenings() {
        return screeningRepository
                .findAll()
                .stream()
                .map(screening ->
                        Optional.of(new ScreeningDto(screening.getMovieTitle(), screening.getRoomName(),
                                screening.getStartDateTime())))
                .toList();
    }
}
