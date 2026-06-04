package fr.iut.rodez.hotel.domain.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * US-02 — Définir un tarif par période
 */
public interface IAddPriceToRoomTypeUseCase {
    void execute(Long roomTypeId, LocalDate startDate, LocalDate endDate, BigDecimal pricePerNight);
}
