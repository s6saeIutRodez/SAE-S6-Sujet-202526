package fr.iut.rodez.hotel.infrastructure.api.dto;

import fr.iut.rodez.hotel.domain.model.Invoice;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * US-08 — DTO de sortie pour une facture
 * Toutes les données sont en lecture seule — cohérent avec l'immuabilité du domaine.
 */
public record InvoiceResponseDto(
        Long id,
        String invoiceNumber,
        Long bookingId,
        String clientNom,
        String clientPrenom,
        String clientEmail,
        String roomTypeName,
        LocalDate fromDate,
        LocalDate toDate,
        int quantity,
        BigDecimal amount,
        LocalDateTime issuedAt
) {
    public static InvoiceResponseDto from(Invoice inv) {
        return new InvoiceResponseDto(
                inv.getId(),
                inv.getInvoiceNumber(),
                inv.getBookingId(),
                inv.getClientNom(),
                inv.getClientPrenom(),
                inv.getClientEmail(),
                inv.getRoomTypeName(),
                inv.getFromDate(),
                inv.getToDate(),
                inv.getQuantity(),
                inv.getAmount(),
                inv.getIssuedAt()
        );
    }
}
