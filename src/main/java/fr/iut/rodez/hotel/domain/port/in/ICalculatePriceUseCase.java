package fr.iut.rodez.hotel.domain.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * US-06 — Calculer le prix d'un séjour
 */
public interface ICalculatePriceUseCase {

    Result execute(Command command);

    record Command(Long roomTypeId, LocalDate from, LocalDate to, int quantity) {}

    record Result(BigDecimal totalPrice, int nights, int quantity) {}
}