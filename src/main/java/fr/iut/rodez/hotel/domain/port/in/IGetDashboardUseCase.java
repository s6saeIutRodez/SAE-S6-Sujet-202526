package fr.iut.rodez.hotel.domain.port.in;

import java.math.BigDecimal;
import java.util.Map;

/**
 * US-07 — Consulter le tableau de bord
 */
public interface IGetDashboardUseCase {

    Result execute();

    record Result(
            long totalBookings,
            BigDecimal totalRevenue,
            double occupancyRate,
            Map<String, Long> bookingsByStatus,
            Map<String, BigDecimal> revenueByRoomType
    ) {}
}