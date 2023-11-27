package com.epam.training.ticketservice.dtos;

import lombok.Builder;

@Builder
public record MovieDto(String title, String genre, int length) {
}
