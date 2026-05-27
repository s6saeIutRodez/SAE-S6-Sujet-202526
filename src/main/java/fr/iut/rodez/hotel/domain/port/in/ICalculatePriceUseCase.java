package fr.iut.rodez.hotel.domain.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * US-06 — Calculer le prix d'un séjour
 */
public interface ICalculatePriceUseCase {
    BigDecimal execute(Long roomTypeId, LocalDate from, LocalDate to, int quantity);
}
