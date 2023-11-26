package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.ScreeningDto;

import java.util.List;
import java.util.Optional;

public interface ScreeningService {
    Optional<ScreeningDto> createScreening(String movieTitle, String roomName, String startDateTime);

    Optional<ScreeningDto> updateScreening(String movieTitle, String roomName, String startDateTime);

    void deleteScreening(String movieTitle, String roomName, String startDateTime);

    List<Optional<ScreeningDto>> listScreenings();
}
