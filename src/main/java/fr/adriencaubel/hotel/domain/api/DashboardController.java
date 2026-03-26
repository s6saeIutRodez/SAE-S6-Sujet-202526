package fr.adriencaubel.hotel.domain.api;


import fr.adriencaubel.hotel.domain.api.dto.DashboardResponse;
import fr.adriencaubel.hotel.domain.Booking;
import fr.adriencaubel.hotel.domain.BookingOption;
import fr.adriencaubel.hotel.domain.RoomType;
import fr.adriencaubel.hotel.domain.port.BookingRepository;
import fr.adriencaubel.hotel.domain.port.InventoryRepository;
import fr.adriencaubel.hotel.domain.port.RoomTypeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final BookingRepository bookingRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final InventoryRepository inventoryRepository;

    public DashboardController(BookingRepository bookingRepository,
                               RoomTypeRepository roomTypeRepository,
                               InventoryRepository inventoryRepository) {
        this.bookingRepository = bookingRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping
    public DashboardResponse getDashboard() {

        List<Booking> bookings = bookingRepository.findAll();

        long totalBookings = bookings.size();

        BigDecimal totalRevenue = bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()))
                .map(Booking::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Count by status
        Map<String, Long> bookingsByStatus = bookings.stream()
                .collect(Collectors.groupingBy(
                        Booking::getStatus,
                        Collectors.counting()
                ));

        Map<String, BigDecimal> revenueByRoomType = new HashMap<>();

        for (Booking b : bookings) {

            List<BookingOption> options = b.getOptions();
            options.forEach(o -> System.out.println(o.getType()));

            RoomType roomType = roomTypeRepository
                    .findById(b.getId())
                    .orElse(null);

            if (roomType == null) continue;

            revenueByRoomType.merge(
                    roomType.getName(),
                    b.getAmount(),
                    BigDecimal::add
            );
        }

        LocalDate today = LocalDate.now();

        int totalCapacity = 0;
        int reservedToday = 0;

        List<RoomType> roomTypes = roomTypeRepository.findAll();

        for (RoomType rt : roomTypes) {

            totalCapacity += rt.getTotalRooms();

            Integer reserved = inventoryRepository
                    .findByRoomTypeAndDateBetween(rt.getId(), today, today).size();

            if (reserved != null) {
                reservedToday += reserved;
            }
        }

        double occupancyRate = totalCapacity == 0
                ? 0
                : ((double) reservedToday / totalCapacity) * 100;

        return new DashboardResponse(
                totalBookings,
                totalRevenue,
                occupancyRate,
                bookingsByStatus,
                revenueByRoomType
        );
    }
}