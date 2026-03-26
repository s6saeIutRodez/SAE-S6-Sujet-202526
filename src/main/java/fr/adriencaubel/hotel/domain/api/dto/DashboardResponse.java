package fr.adriencaubel.hotel.domain.api.dto;

import java.math.BigDecimal;
import java.util.Map;

public class DashboardResponse {

    public long totalBookings;
    public BigDecimal totalRevenue;
    public double occupancyRate;
    public Map<String, Long> bookingsByStatus;
    public Map<String, BigDecimal> revenueByRoomType;

    public DashboardResponse(long totalBookings,
                             BigDecimal totalRevenue,
                             double occupancyRate,
                             Map<String, Long> bookingsByStatus,
                             Map<String, BigDecimal> revenueByRoomType) {
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
        this.occupancyRate = occupancyRate;
        this.bookingsByStatus = bookingsByStatus;
        this.revenueByRoomType = revenueByRoomType;
    }
}