package fr.iut.rodez.hotel.infrastructure.api.dto;

import fr.iut.rodez.hotel.domain.model.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BookingResponseDto(
        Long id,
        Long roomTypeId,
        LocalDate fromDate,
        LocalDate toDate,
        int quantity,
        BigDecimal amount,
        String nom,
        String prenom,
        String email,
        String status,
        List<String> options
) {
    public static BookingResponseDto from(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getRoomType().getId(),
                booking.getFromDate(),
                booking.getToDate(),
                booking.getQuantity(),
                booking.getAmount(),
                booking.getNom(),
                booking.getPrenom(),
                booking.getEmail(),
                booking.getStatus(),
                booking.getOptions().stream()
                        .map(o -> o.getType() + (o.getComment() != null ? "," + o.getComment() : ""))
                        .toList()
        );
    }
}