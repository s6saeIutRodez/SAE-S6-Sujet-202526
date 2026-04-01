package fr.iut.rodez.hotel.domain.service;

import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.model.RoomTypePrice;
import java.math.BigDecimal;
import java.time.LocalDate;

// Logique métier pure — pas de dépendance infra, testable sans mock
public class PricingDomainService {

    public BigDecimal calculateTotalPrice(RoomType roomType,
                                          LocalDate from, LocalDate to,
                                          int quantity) {
        BigDecimal total = BigDecimal.ZERO;
        LocalDate current = from;

        while (current.isBefore(to)) {
            BigDecimal nightPrice = getPriceForDate(roomType, current);
            total = total.add(nightPrice.multiply(BigDecimal.valueOf(quantity)));
            current = current.plusDays(1);
        }

        return total;
    }

    public BigDecimal getPriceForDate(RoomType roomType, LocalDate date) {
        return roomType.getPrices().stream()
                .filter(p -> p.covers(date))
                .map(RoomTypePrice::getPricePerNight)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Aucun tarif défini pour le " + date +
                                " sur le type de chambre " + roomType.getName()
                ));
    }
}