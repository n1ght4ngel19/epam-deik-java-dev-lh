package com.epam.training.ticketservice.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScreeningDto(String title, String genre, int length, String room, LocalDateTime startTime) {
}
