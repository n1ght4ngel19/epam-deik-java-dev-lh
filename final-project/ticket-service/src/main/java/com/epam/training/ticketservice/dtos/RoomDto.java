package com.epam.training.ticketservice.dtos;

import lombok.Builder;

@Builder
public record RoomDto(String name, int rows, int columns) {
}
