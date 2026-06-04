package fr.iut.rodez.hotel.infrastructure.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * US-01 — DTO d'entrée pour la création d'un type de chambre
 */
public record RoomTypeRequestDto(
        @NotBlank String name,
        @Min(1) int totalRooms
) {}
