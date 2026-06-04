package fr.iut.rodez.hotel.application.usecase;

import fr.iut.rodez.hotel.domain.model.Booking;
import fr.iut.rodez.hotel.domain.model.BookingStatus;
import fr.iut.rodez.hotel.domain.model.RoomType;
import fr.iut.rodez.hotel.domain.port.in.IGetDashboardUseCase;
import fr.iut.rodez.hotel.domain.port.out.BookingRepository;
import fr.iut.rodez.hotel.domain.port.out.InventoryRepository;
import fr.iut.rodez.hotel.domain.port.out.RoomTypeRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GetDashboardUseCase implements IGetDashboardUseCase {

    private final BookingRepository bookingRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final InventoryRepository inventoryRepository;

    public GetDashboardUseCase(BookingRepository bookingRepository,
                               RoomTypeRepository roomTypeRepository,
                               InventoryRepository inventoryRepository) {
        this.bookingRepository   = bookingRepository;
        this.roomTypeRepository  = roomTypeRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Result execute() {
        List<Booking>  bookings  = bookingRepository.findAll();
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        LocalDate today = LocalDate.now();

        long totalBookings = bookings.size();

        BigDecimal totalRevenue = bookings.stream()
                .filter(b -> BookingStatus.CONFIRMED.name().equals(b.getStatus()))
                .map(Booking::getAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> bookingsByStatus = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getStatus, Collectors.counting()));

        Map<String, BigDecimal> revenueByRoomType = bookings.stream()
                .filter(b -> BookingStatus.CONFIRMED.name().equals(b.getStatus())
                        && b.getAmount() != null)
                .collect(Collectors.groupingBy(
                        b -> b.getRoomType().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Booking::getAmount, BigDecimal::add)));

        int totalCapacity = roomTypes.stream().mapToInt(RoomType::getTotalRooms).sum();

        int reservedToday = roomTypes.stream()
                .mapToInt(rt -> inventoryRepository
                        .findByRoomTypeIdAndDateBetween(rt.getId(), today, today)
                        .stream().mapToInt(inv -> inv.getReservedRooms()).sum())
                .sum();

        double occupancyRate = totalCapacity == 0 ? 0
                : ((double) reservedToday / totalCapacity) * 100;

        return new Result(totalBookings, totalRevenue, occupancyRate,
                bookingsByStatus, revenueByRoomType);
    }
}