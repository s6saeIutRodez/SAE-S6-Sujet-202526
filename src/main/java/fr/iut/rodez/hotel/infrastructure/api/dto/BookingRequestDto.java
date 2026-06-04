package fr.iut.rodez.hotel.infrastructure.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record BookingRequestDto(
        @NotNull Long roomTypeId,
        @NotNull LocalDate from,
        @NotNull LocalDate to,
        @Min(1) int quantity,
        @NotBlank String nom,
        @NotBlank String prenom,
        @Email @NotBlank String email,
        List<String> options
) {}
