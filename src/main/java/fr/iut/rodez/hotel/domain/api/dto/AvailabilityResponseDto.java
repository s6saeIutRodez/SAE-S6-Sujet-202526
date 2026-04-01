package fr.iut.rodez.hotel.domain.api.dto;

import fr.iut.rodez.hotel.application.usecase.CheckAvailabilityUseCase;
import java.time.LocalDate;

public record AvailabilityResponseDto(
        Long roomTypeId,
        LocalDate from,
        LocalDate to,
        boolean available,
        int remainingRooms
) {
    public static AvailabilityResponseDto from(CheckAvailabilityUseCase.Result result) {
        return new AvailabilityResponseDto(
                result.roomTypeId(), result.from(), result.to(),
                result.available(), result.remainingRooms()
        );
    }
}