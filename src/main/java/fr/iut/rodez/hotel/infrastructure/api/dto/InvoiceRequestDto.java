package fr.iut.rodez.hotel.infrastructure.api.dto;

public record InvoiceRequestDto(
        Long bookingId,
        Double tvaRate
) {}
