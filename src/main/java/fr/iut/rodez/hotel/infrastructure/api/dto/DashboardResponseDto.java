package fr.iut.rodez.hotel.infrastructure.api.dto;

import fr.iut.rodez.hotel.application.usecase.GetDashboardUseCase;
import java.math.BigDecimal;
import java.util.Map;

/**
 * US-07 — Tableau de bord
 * DTO de sortie — ne sort jamais d'objets domaine de la couche infra.
 */
public record DashboardResponseDto(
        long totalBookings,
        BigDecimal totalRevenue,
        double occupancyRate,
        Map<String, Long> bookingsByStatus,
        Map<String, BigDecimal> revenueByRoomType
) {
    public static DashboardResponseDto from(GetDashboardUseCase.Result result) {
        return new DashboardResponseDto(
                result.totalBookings(),
                result.totalRevenue(),
                result.occupancyRate(),
                result.bookingsByStatus(),
                result.revenueByRoomType()
        );
    }
}
