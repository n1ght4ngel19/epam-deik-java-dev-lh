package com.epam.training.ticketservice.services;

import com.epam.training.ticketservice.dtos.ScreeningDto;
import com.epam.training.ticketservice.exceptions.MovieDoesNotExistException;
import com.epam.training.ticketservice.exceptions.ScreeningAlreadyExistsException;
import com.epam.training.ticketservice.exceptions.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.exceptions.ScreeningOverlapException;

import java.util.List;
import java.util.Optional;

public interface ScreeningService {
    void createScreening(String movieTitle, String roomName, String startDateTime)
            throws ScreeningAlreadyExistsException, MovieDoesNotExistException, ScreeningOverlapException;

    void updateScreening(String movieTitle, String roomName, String startDateTime)
            throws ScreeningDoesNotExistException;

    void deleteScreening(String movieTitle, String roomName, String startDateTime)
            throws ScreeningDoesNotExistException;

    List<Optional<ScreeningDto>> listScreenings()
            throws ScreeningDoesNotExistException;
}
