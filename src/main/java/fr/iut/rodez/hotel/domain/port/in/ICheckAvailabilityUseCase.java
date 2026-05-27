package fr.iut.rodez.hotel.domain.port.in;

import fr.iut.rodez.hotel.application.usecase.CheckAvailabilityUseCase;
import java.time.LocalDate;

/**
 * US-03 — Consulter la disponibilité
 */
public interface ICheckAvailabilityUseCase {
    CheckAvailabilityUseCase.Result execute(Long roomTypeId, LocalDate from, LocalDate to, int qty);
}
