package fr.iut.rodez.hotel.domain.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * US-01 — Consulter les types de chambres
 */
public interface IGetRoomTypeUseCase {

    Result execute(Long id);
    List<Result> executeAll();

    record Result(Long id, String name, int totalRooms, List<PriceResult> prices) {}

    record PriceResult(LocalDate startDate, LocalDate endDate, BigDecimal pricePerNight) {}
}