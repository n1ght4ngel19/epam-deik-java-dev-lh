package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.ScreeningDto;
import com.epam.training.ticketservice.exceptions.CreationException;

import java.util.List;
import java.util.Optional;

public interface ScreeningService {
    Optional<ScreeningDto> createScreening(String movieTitle, String roomName, String startDateTime) throws CreationException;

    Optional<ScreeningDto> updateScreening(String movieTitle, String roomName, String startDateTime);

    void deleteScreening(String movieTitle, String roomName, String startDateTime);

    List<Optional<ScreeningDto>> listScreenings();
}
