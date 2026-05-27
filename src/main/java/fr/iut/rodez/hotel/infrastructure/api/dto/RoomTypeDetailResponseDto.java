package fr.iut.rodez.hotel.infrastructure.api.dto;

import fr.iut.rodez.hotel.application.usecase.GetRoomTypeUseCase;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * US-01 — DTO de sortie pour un type de chambre avec ses tarifs
 */
public record RoomTypeDetailResponseDto(
        Long id,
        String name,
        int totalRooms,
        List<PriceDto> prices
) {
    public record PriceDto(
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal pricePerNight
    ) {}

    public static RoomTypeDetailResponseDto from(GetRoomTypeUseCase.Result result) {
        List<PriceDto> prices = result.prices().stream()
                .map(p -> new PriceDto(p.startDate(), p.endDate(), p.pricePerNight()))
                .toList();
        return new RoomTypeDetailResponseDto(result.id(), result.name(), result.totalRooms(), prices);
    }
}
