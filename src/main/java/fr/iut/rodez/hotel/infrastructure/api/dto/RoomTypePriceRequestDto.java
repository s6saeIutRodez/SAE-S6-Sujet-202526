package fr.iut.rodez.hotel.infrastructure.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * US-02 — DTO d'entrée pour la définition d'un tarif par période
 */
public record RoomTypePriceRequestDto(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @DecimalMin("0.01") BigDecimal pricePerNight
) {}
