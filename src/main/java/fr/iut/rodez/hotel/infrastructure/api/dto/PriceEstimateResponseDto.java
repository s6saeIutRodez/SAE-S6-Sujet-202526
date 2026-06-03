package fr.iut.rodez.hotel.infrastructure.api.dto;

import fr.iut.rodez.hotel.domain.port.in.ICalculatePriceUseCase;
import java.math.BigDecimal;

public record PriceEstimateResponseDto(
        BigDecimal totalPrice,
        int nights,
        int quantity
) {
    public static PriceEstimateResponseDto from(ICalculatePriceUseCase.Result result) {
        return new PriceEstimateResponseDto(
                result.totalPrice(),
                result.nights(),
                result.quantity()
        );
    }
}