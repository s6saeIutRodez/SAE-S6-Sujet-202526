package fr.iut.rodez.hotel.domain.port.in;

import java.time.LocalDate;

/**
 * US-03 — Consulter la disponibilité
 */
public interface ICheckAvailabilityUseCase {

    Result execute(Long roomTypeId, LocalDate from, LocalDate to, int qty);

    record Result(
            Long roomTypeId,
            LocalDate from,
            LocalDate to,
            boolean available,
            int remainingRooms
    ) {}
}